package com.NindyBun.ByAMedium.helpers;

import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;

public class SoundTicker extends AbstractTickableSoundInstance {

    public SoundTicker(BlockPos pos, float volume, float pitch, SoundEvent soundEvent, SoundSource soundSource, RandomSource source) {
        super(soundEvent, soundSource, source);
        this.x = pos.getX();
        this.y = pos.getY();
        this.z = pos.getZ();
        this.looping = true;
        this.delay = 0;
        this.volume = volume;
        this.pitch = pitch;
    }

    @Override
    public void tick() {

    }

    public void stopPlaying() {
        this.stop();
    }

}
