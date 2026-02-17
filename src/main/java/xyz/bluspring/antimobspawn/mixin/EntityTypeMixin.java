package xyz.bluspring.antimobspawn.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.bluspring.antimobspawn.AntiMobSpawn;

import java.util.function.Consumer;

@Mixin(EntityType.class)
public class EntityTypeMixin<T extends Entity> {
    @Inject(at = @At("RETURN"), method = "spawn(Lnet/minecraft/server/level/ServerLevel;Ljava/util/function/Consumer;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/entity/EntitySpawnReason;ZZ)Lnet/minecraft/world/entity/Entity;")
    public void antimobspawn$disableSpawn(ServerLevel serverLevel, @Nullable Consumer<T> consumer, BlockPos blockPos, EntitySpawnReason entitySpawnReason, boolean bl, boolean bl2, CallbackInfoReturnable<T> cir) {
        var entity = cir.getReturnValue();

        if (entity == null)
            return;

        if (!AntiMobSpawn.config.allowSpawn(entity, entitySpawnReason)) {
            entity.remove(Entity.RemovalReason.DISCARDED);
        }
    }
}