package com.itemseeker.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketTeleportToResult implements IMessage {

    private BlockPos targetPos = BlockPos.ORIGIN;
    private int dimensionId = 0;

    public PacketTeleportToResult() {
    }

    public PacketTeleportToResult(BlockPos targetPos, int dimensionId) {
        this.targetPos = targetPos;
        this.dimensionId = dimensionId;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        targetPos = BlockPos.fromLong(buf.readLong());
        dimensionId = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeLong(targetPos.toLong());
        buf.writeInt(dimensionId);
    }

    public static class Handler implements IMessageHandler<PacketTeleportToResult, IMessage> {

        @Override
        public IMessage onMessage(PacketTeleportToResult message, MessageContext ctx) {
            EntityPlayerMP player = ctx.getServerHandler().player;
            WorldServer world = player.getServerWorld();

            world.addScheduledTask(() -> {
                BlockPos target = message.targetPos;
                BlockPos tpPos = target.up();
                if (!world.isAirBlock(tpPos)) {
                    if (world.isAirBlock(target.north())) tpPos = target.north();
                    else if (world.isAirBlock(target.south())) tpPos = target.south();
                    else if (world.isAirBlock(target.east())) tpPos = target.east();
                    else if (world.isAirBlock(target.west())) tpPos = target.west();
                    else tpPos = target.up(2);
                }

                player.connection.setPlayerLocation(
                    tpPos.getX() + 0.5,
                    tpPos.getY() + 0.1,
                    tpPos.getZ() + 0.5,
                    player.rotationYaw,
                    player.rotationPitch
                );

                world.playSound(
                    null,
                    player.posX,
                    player.posY,
                    player.posZ,
                    SoundEvents.ENTITY_ENDERMEN_TELEPORT,
                    SoundCategory.PLAYERS,
                    1.0F,
                    1.0F
                );

                player.sendMessage(new TextComponentString(
                    TextFormatting.GREEN + "[Жезл Поиска] Телепортация к контейнеру на ["
                        + target.getX() + ", " + target.getY() + ", " + target.getZ() + "]"
                ));
            });

            return null;
        }
    }
}
