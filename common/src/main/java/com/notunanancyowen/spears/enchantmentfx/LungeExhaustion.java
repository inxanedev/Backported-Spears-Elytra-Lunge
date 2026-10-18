package com.notunanancyowen.spears.enchantmentfx;

import com.mojang.serialization.MapCodec;
import com.notunanancyowen.spears.Spears;
import net.minecraft.enchantment.EnchantmentEffectContext;
import net.minecraft.enchantment.effect.EnchantmentEntityEffect;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;

public record LungeExhaustion() implements EnchantmentEntityEffect {
    public static final MapCodec<LungeExhaustion> CODEC = MapCodec.unit(new LungeExhaustion());

    @Override
    public void apply(ServerWorld world, int level, EnchantmentEffectContext context, Entity user, Vec3d pos) {
        if (user instanceof PlayerEntity playerEntity) {
            playerEntity.addExhaustion(Spears.lungeExhaustionPerLevel * level);
        }
    }

    @Override
    public MapCodec<LungeExhaustion> getCodec() {
        return CODEC;
    }
}
