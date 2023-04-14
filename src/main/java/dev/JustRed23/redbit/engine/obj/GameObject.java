package dev.JustRed23.redbit.engine.obj;

import org.jetbrains.annotations.Nullable;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class GameObject {

    private final String name;
    private final List<Component> components;

    public Transform transform;

    public GameObject(String name) {
        this.name = name;
        this.components = new ArrayList<>();
        this.transform = new Transform();
    }

    public GameObject(String name, Transform transform) {
        this(name);
        this.transform = transform;
    }

    public GameObject copy(String copyName) {
        GameObject gameObjectCopy = new GameObject(copyName);
        for (Component component : components)
            gameObjectCopy.addComponent(component.copy(gameObjectCopy));
        return gameObjectCopy;
    }

    public void init() {
        for (int i = 0; i < components.size(); i++)
            components.get(i).init();
    }

    public void update() {
        for (int i = 0; i < components.size(); i++)
            components.get(i).update();
    }

    public final <T extends Component> @Nullable T getComponent(Class<T> clazz) {
        for (Component component : components) {
            if (clazz.isAssignableFrom(component.getClass()))
                try {
                    return clazz.cast(component);
                } catch (ClassCastException e) {
                    LoggerFactory.getLogger(GameObject.class).error("Failed to cast component. THIS SHOULD NEVER HAPPEN!", e);
                    return null;
                }
        }
        return null;
    }

    public final <T extends Component> void removeComponent(Class<T> clazz) {
        for (int i = 0; i < components.size(); i++) {
            Component component = components.get(i);
            if (clazz.isAssignableFrom(component.getClass())) {
                components.remove(i);
                return;
            }
        }
    }

    public final void addComponent(Component component) {
        if (component == null)
            return;

        component.parent = this;
        components.add(component);
    }

    public final String getName() {
        return name;
    }
}
