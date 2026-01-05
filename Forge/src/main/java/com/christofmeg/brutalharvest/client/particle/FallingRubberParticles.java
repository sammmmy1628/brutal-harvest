package com.christofmeg.brutalharvest.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@OnlyIn(Dist.CLIENT)
public class FallingRubberParticles extends TextureSheetParticle {

    protected FallingRubberParticles(ClientLevel pLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed, SpriteSet spriteSet) {
        super(pLevel, pX, pY, pZ, pXSpeed, pYSpeed, pZSpeed);
        this.setSpriteFromAge(spriteSet);
        this.setParticleSpeed(pXSpeed, pYSpeed, pZSpeed);
        this.setLifetime(20);
    }

    @Override
    public @NotNull ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    @OnlyIn(Dist.CLIENT)
    public static class Provider implements ParticleProvider<SimpleParticleType> {

        private final SpriteSet spriteSet;

        public Provider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Override
        public @Nullable Particle createParticle(@NotNull SimpleParticleType simpleParticleType, @NotNull ClientLevel clientLevel, double v, double v1, double v2, double v3, double v4, double v5) {
            return new FallingRubberParticles(clientLevel, v, v1, v2, v3, v4, v5, this.spriteSet);
        }
    }
}
