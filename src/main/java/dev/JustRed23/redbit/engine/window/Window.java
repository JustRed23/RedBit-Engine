package dev.JustRed23.redbit.engine.window;

import dev.JustRed23.redbit.engine.Engine;
import dev.JustRed23.redbit.engine.callback.CallbackController;
import dev.JustRed23.redbit.engine.err.WindowInitException;
import dev.JustRed23.redbit.engine.utils.FileUtils;
import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.IntBuffer;
import java.util.concurrent.TimeUnit;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {

    private static final Logger LOGGER = LoggerFactory.getLogger(Window.class);

    private final WindowOptions options;
    private int centerX, centerY, width, height;

    private boolean visible, focused = true;
    private boolean fullscreen;

    private long windowHandle;

    private View currentView, lastView;

    private Runnable onUpdate = () -> {};
    private Runnable onRender = () -> {};

    Window(WindowOptions options) {
        this.options = options;
    }

    void setup() throws WindowInitException {
        if (windowHandle != 0)
            throw new IllegalStateException("Window is already initialized");

        if (!glfwInit())
            throw new WindowInitException("GLFW failed to initialize");

        this.width = options.width();
        this.height = options.height();

        glfwWindowHint(GLFW_RESIZABLE, options.resizable() ? GLFW_TRUE : GLFW_FALSE);

        if (options.fullscreen())
            toggleFullscreen();

        windowHandle = glfwCreateWindow(width, height, options.title(), options.fullscreen() ? glfwGetPrimaryMonitor() : NULL, NULL);

        if (options.iconPath() != null) {
            GLFWImage image = FileUtils.loadImage(options.iconPath());

            try (GLFWImage.Buffer buffer = GLFWImage.malloc(1)) {
                buffer.put(0, image);
                glfwSetWindowIcon(windowHandle, buffer);
            }
        }

        if (windowHandle == NULL)
            throw new WindowInitException("GLFW failed to create a window");

        createCallbacks();

        try (MemoryStack stack = stackPush()) {
            IntBuffer pWidth = stack.mallocInt(1); // int*
            IntBuffer pHeight = stack.mallocInt(1); // int*

            // Get the window size passed to glfwCreateWindow
            glfwGetWindowSize(windowHandle, pWidth, pHeight);

            // Get the resolution of the primary monitor
            GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

            if (vidmode == null)
                throw new WindowInitException("GLFW failed to get the primary monitor");

            this.centerX = (vidmode.width() - pWidth.get(0)) / 2;
            this.centerY = (vidmode.height() - pHeight.get(0)) / 2;

            // Center the window
            glfwSetWindowPos(windowHandle, centerX, centerY);


            // Make the OpenGL context current
            glfwMakeContextCurrent(windowHandle);
            // Enable v-sync
            glfwSwapInterval(1);
        }

        show();

        GL.createCapabilities();

        glEnable(GL_BLEND);
        glBlendFunc(GL_ONE, GL_ONE_MINUS_SRC_ALPHA);

        LOGGER.info("Screen " + options.title() + " initialized (took {}ms)", TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - Engine.START_TIME));
    }

    private void createCallbacks() {
        if (windowHandle == 0)
            throw new IllegalStateException("Window is not initialized");

        glfwSetWindowFocusCallback(windowHandle, (handle, focused) -> {
            if (handle == windowHandle && windowHandle != 0)
                this.focused = focused;
        });

        glfwSetKeyCallback(windowHandle, (handle, key, scancode, action, mods) -> {
            if (handle == windowHandle && windowHandle != 0)
                CallbackController.keyCallback(this, key, action, mods);
        });

        glfwSetMouseButtonCallback(windowHandle, (handle, button, action, mods) -> {
            if (handle == windowHandle && windowHandle != 0)
                CallbackController.mouseButtonCallback(this, button, action, mods);
        });

        glfwSetCursorPosCallback(windowHandle, (handle, xpos, ypos) -> {
            if (handle == windowHandle && windowHandle != 0)
                CallbackController.mousePosCallback(this, xpos, ypos);
        });

        glfwSetScrollCallback(windowHandle, (handle, xoffset, yoffset) -> {
            if (handle == windowHandle && windowHandle != 0)
                CallbackController.mouseScrollCallback(this, yoffset);
        });

        if (options.resizable()) {
            glfwSetFramebufferSizeCallback(windowHandle, (handle, width, height) -> {
                if (handle == windowHandle && windowHandle != 0) {
                    glViewport(0, 0, width, height);
                    this.width = width;
                    this.height = height;
                    if (currentView != null)
                        currentView.camera.updateProjection(width, height);
                }
            });
        }
    }

    void update() {
        if (glfwWindowShouldClose(windowHandle)) {
            destroy();
            return;
        }

        if (currentView != null) {
            try {
                currentView._update();
            } catch (Exception e) {
                LOGGER.error("An error occurred while updating a view (" + currentView.getClass().getSimpleName() + ")", e);
                setView(null);
            }
        }

        onUpdate.run();
    }

    void render() {
        if (!visible || !focused)
            return;

        glClear(GL_COLOR_BUFFER_BIT);
        glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

        if (currentView != null) {
            try {
                currentView._render();
            } catch (Exception e) {
                LOGGER.error("An error occurred while rendering a view (" + currentView.getClass().getSimpleName() + ")", e);
                setView(null);
            }
        }

        onRender.run();
    }

    void swapBuffers() {
        if (visible)
            glfwSwapBuffers(windowHandle);
    }

    public void destroy() {
        glfwFreeCallbacks(windowHandle);
        CallbackController.clearCallbacks(this);

        glfwDestroyWindow(windowHandle);
        windowHandle = 0;

        WindowController.checkIfLastWindow(options.exitOnClose());
    }

    public void show() {
        glfwShowWindow(windowHandle);
        visible = true;
    }

    public void hide() {
        glfwHideWindow(windowHandle);
        visible = false;
    }

    public void toggleFullscreen() {
        fullscreen = !fullscreen;

        this.width = fullscreen ? glfwGetVideoMode(glfwGetPrimaryMonitor()).width() : options.width();
        this.height = fullscreen ? glfwGetVideoMode(glfwGetPrimaryMonitor()).height() : options.height();

        glfwSetWindowMonitor(windowHandle, fullscreen ? glfwGetPrimaryMonitor() : NULL, fullscreen ? 0 : centerX, fullscreen ? 0 : centerY, width, height, GLFW_DONT_CARE);
    }

    public void setGlobalUpdate(Runnable onUpdate) {
        this.onUpdate = onUpdate;
    }

    public void setGlobalRender(Runnable onRender) {
        this.onRender = onRender;
    }

    public boolean setView(View view) {
        try {
            if (view != null)
                view._setup(this);
        } catch (Exception e) {
            LOGGER.error("Failed to setup view: " + view.getClass().getSimpleName(), e);
            return false;
        }

        if (currentView != null)
            currentView._cleanup();

        lastView = currentView;
        currentView = view;
        return true;
    }

    public void setTitle(String title) {
        glfwSetWindowTitle(windowHandle, title);
    }

    public View getCurrentView() {
        return currentView;
    }

    public View getLastView() {
        return lastView;
    }

    public long getWindowHandle() {
        return windowHandle;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
