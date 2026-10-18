package com.notunanancyowen.spears.fabric.client;

import com.notunanancyowen.spears.Spears;
import com.notunanancyowen.spears.fabric.SpearsFabric;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.text.Text;

import java.util.Locale;

public class SpearsConfigScreen extends Screen {
    private static final float MIN_EXHAUSTION = 0.0F;
    private static final float MAX_EXHAUSTION = 20.0F;
    private final Screen parent;
    private boolean restrictLungeWhileElytraFlying;
    private float lungeExhaustionPerLevel;

    public SpearsConfigScreen(Screen parent) {
        super(Text.translatable("spears.config.title"));
        this.parent = parent;
        this.restrictLungeWhileElytraFlying = Spears.restrictLungeWhileElytraFlying;
        this.lungeExhaustionPerLevel = Spears.lungeExhaustionPerLevel;
    }

    @Override
    protected void init() {
        int center = this.width / 2;
        int top = this.height / 2 - 40;

        this.addDrawableChild(ButtonWidget.builder(this.restrictionText(), button -> {
            this.restrictLungeWhileElytraFlying = !this.restrictLungeWhileElytraFlying;
            button.setMessage(this.restrictionText());
        }).dimensions(center - 150, top, 300, 20).build());

        this.addDrawableChild(new ExhaustionSlider(center - 150, top + 30, 300, 20, this.lungeExhaustionPerLevel, value -> this.lungeExhaustionPerLevel = value));

        this.addDrawableChild(ButtonWidget.builder(Text.translatable("spears.config.save"), button -> {
            Spears.restrictLungeWhileElytraFlying = this.restrictLungeWhileElytraFlying;
            Spears.lungeExhaustionPerLevel = this.lungeExhaustionPerLevel;
            SpearsFabric.saveConfig();
            this.client.setScreen(this.parent);
        }).dimensions(center - 150, top + 70, 145, 20).build());

        this.addDrawableChild(ButtonWidget.builder(Text.translatable("spears.config.cancel"), button -> this.client.setScreen(this.parent))
                .dimensions(center + 5, top + 70, 145, 20).build());
    }

    private Text restrictionText() {
        return Text.translatable("spears.config.restrict_elytra_lunge",
                Text.translatable(this.restrictLungeWhileElytraFlying ? "spears.config.on" : "spears.config.off"));
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 20, 0xFFFFFF);
    }

    private static class ExhaustionSlider extends SliderWidget {
        private final java.util.function.Consumer<Float> onChange;
        private boolean ready;

        private ExhaustionSlider(int x, int y, int width, int height, float value, java.util.function.Consumer<Float> onChange) {
            super(x, y, width, height, Text.empty(), (value - MIN_EXHAUSTION) / (MAX_EXHAUSTION - MIN_EXHAUSTION));
            this.onChange = onChange;
            this.ready = true;
            this.updateMessage();
        }

        private float value() {
            float value = (float) (MIN_EXHAUSTION + this.value * (MAX_EXHAUSTION - MIN_EXHAUSTION));
            return Math.round(value * 2.0F) / 2.0F;
        }

        @Override
        protected void updateMessage() {
            this.setMessage(Text.translatable("spears.config.lunge_exhaustion", String.format(Locale.ROOT, "%.1f", this.value())));
        }

        @Override
        protected void applyValue() {
            if (this.ready) {
                this.onChange.accept(this.value());
            }
        }
    }
}
