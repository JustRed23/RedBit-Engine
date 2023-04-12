package dev.JustRed23.redbit.engine.window;

import dev.JustRed23.redbit.engine.Engine;
import dev.JustRed23.redbit.engine.err.WindowInitException;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.glfw.GLFWWindowFocusCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.IntBuffer;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {

    private static final Logger LOGGER = LoggerFactory.getLogger(Window.class);

    private final WindowOptions options;
    private boolean visible, focused = true;

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

        glfwWindowHint(GLFW_RESIZABLE, options.resizable() ? GLFW_TRUE : GLFW_FALSE);

        windowHandle = glfwCreateWindow(options.width(), options.height(), options.title(), NULL, NULL);

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

            // Center the window
            glfwSetWindowPos(
                    windowHandle,
                    (vidmode.width() - pWidth.get(0)) / 2,
                    (vidmode.height() - pHeight.get(0)) / 2
            );


            // Make the OpenGL context current
            glfwMakeContextCurrent(windowHandle);
            // Enable v-sync
            glfwSwapInterval(0);
        }

        show();

        GL.createCapabilities();

        LOGGER.info("Screen " + options.title() + " initialized (took {}ms)", System.currentTimeMillis() - Engine.START_TIME);
    }

    private void createCallbacks() {
        if (windowHandle == 0)
            throw new IllegalStateException("Window is not initialized");

        glfwSetWindowFocusCallback(windowHandle, (handle, focused) -> {
            if (handle == windowHandle && windowHandle != 0)
                this.focused = focused;
        });
    }

    void update() {
        if (windowHandle == 0)
            return;

        if (glfwWindowShouldClose(windowHandle)) {
            destroy();
            return;
        }

        if (currentView != null)
            currentView.update();

        onUpdate.run();
    }

    void render() {
        if (windowHandle == 0 || !visible || !focused)
            return;

        glEnable(GL_DEPTH_TEST);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

        if (currentView != null)
            currentView.render();

        onRender.run();
    }

    void swapBuffers() {
        if (windowHandle != 0 && visible)
            glfwSwapBuffers(windowHandle);
    }

    public void destroy() {
        glfwFreeCallbacks(windowHandle);
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

    public void setGlobalUpdate(Runnable onUpdate) {
        this.onUpdate = onUpdate;
    }

    public void setGlobalRender(Runnable onRender) {
        this.onRender = onRender;
    }

    public boolean setView(View view) {
        try {
            if (view != null)
                view.setup(this);
        } catch (Exception e) {
            LOGGER.error("Failed to setup view: " + view.getClass().getSimpleName(), e);
            return false;
        }

        if (currentView != null)
            currentView.cleanup();

        lastView = currentView;
        currentView = view;
        return true;
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
}
