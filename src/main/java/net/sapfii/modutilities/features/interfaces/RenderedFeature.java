package net.sapfii.modutilities.features.interfaces;

import net.minecraft.client.gui.DrawContext;
import net.velli.scelli.widget.interfaces.ClickableWidget;
import net.velli.scelli.widget.interfaces.WidgetContainer;

public interface RenderedFeature extends ClickableWidget {

    void render(DrawContext context, int mouseX, int mouseY);
    void hover(int mouseX, int mouseY, boolean active);

    default int x() {
        return 0;
    }


    default int y() {
        return 0;
    }


    default int width() {
        return 0;
    }


    default int height() {
        return 0;
    }


    default int opacity() {
        return 255;
    }
}
