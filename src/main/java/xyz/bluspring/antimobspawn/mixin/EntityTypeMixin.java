package xyz.bluspring.antimobspawn.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.bluspring.antimobspawn.AntiMobSpawn;

import java.util.function.Consumer;

@Mixin(EntityType.class)
public class EntityTypeMixin<T extends Entity> {
    @Inject(at = @At("RETURN"), method = "spawn(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/nbt/CompoundTag;Ljava/util/function/Consumer;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/entity/MobSpawnType;ZZ)Lnet/minecraft/world/entity/Entity;")
    public void antimobspawn$disableSpawn(ServerLevel serverLevel, @Nullable CompoundTag compoundTag, Consumer<T> consumer, BlockPos blockPos, MobSpawnType mobSpawnType, boolean bl, boolean bl2, CallbackInfoReturnable<@Nullable T> cir) {
        var entity = cir.getReturnValue();

        if (entity == null)
            return;

        if (!AntiMobSpawn.config.allowSpawn(entity, mobSpawnType)) {
            entity.remove(Entity.RemovalReason.DISCARDED);
        }
    }
}