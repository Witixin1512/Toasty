package net.witixin.toasty.toasts;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraft.client.gui.components.toasts.ToastComponent;
import net.minecraft.util.FormattedCharSequence;
import net.witixin.toasty.factories.SimpleToastFactory;
import net.witixin.toasty.factories.ToastFactory;

import java.util.List;

public class SimpleToast implements Toast {

    private final SimpleToastFactory factory;
    private final int width;
    private final int height;
    private final List<FormattedCharSequence> messageLines;

    public SimpleToast(SimpleToastFactory factory) {
        this.factory = factory;
        this.messageLines = Minecraft.getInstance().font.split(factory.text(), 200);
        this.height = 20 + Math.max(messageLines.size(), 1) * 12;
        this.width = 30 + Math.max(200, messageLines.stream().mapToInt(Minecraft.getInstance().font::width).max().orElse(200));
    }

    @Override
    public Visibility render(GuiGraphics guiGraphics, ToastComponent pToastComponent, long pTimeSinceLastVisible) {
        guiGraphics.blitSprite(ToastFactory.BACKGROUND_SPRITE, 0, 0, this.width(), this.height());
        guiGraphics.drawString(pToastComponent.getMinecraft().font, factory.title(), 30, 7, 0, false);
        for (int i = 0; i < this.messageLines.size(); i++) {
            guiGraphics.drawString(pToastComponent.getMinecraft().font, this.messageLines.get(i), 30, 18 + i * 12, 0, false);
        }
        guiGraphics.renderFakeItem(factory.stack(), 8, 8);
        return (double) pTimeSinceLastVisible >= factory.displayTimeInMilliseconds() * pToastComponent.getNotificationDisplayTimeMultiplier() ?
                Toast.Visibility.HIDE : Toast.Visibility.SHOW;
    }

    @Override
    public int height() {
        return height;
    }

    @Override
    public int width() {
        return width;
    }
}
