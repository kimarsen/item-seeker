package com.itemseeker.search;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;

public class SearchResultEntry {

    public BlockPos pos;
    public String containerName;
    public int count;
    public double distance;
    public boolean isEntity;
    public int entityId;
    public int dimensionId;

    public SearchResultEntry(
        BlockPos pos,
        String containerName,
        int count,
        double distance,
        boolean isEntity,
        int entityId,
        int dimensionId
    ) {
        this.pos = pos;
        this.containerName = containerName;
        this.count = count;
        this.distance = distance;
        this.isEntity = isEntity;
        this.entityId = entityId;
        this.dimensionId = dimensionId;
    }

    public void writeToBuffer(ByteBuf buf) {
        buf.writeLong(pos.toLong());
        ByteBufUtils.writeUTF8String(buf, containerName != null ? containerName : "Container");
        buf.writeInt(count);
        buf.writeDouble(distance);
        buf.writeBoolean(isEntity);
        buf.writeInt(entityId);
        buf.writeInt(dimensionId);
    }

    public static SearchResultEntry readFromBuffer(ByteBuf buf) {
        BlockPos pos = BlockPos.fromLong(buf.readLong());
        String name = ByteBufUtils.readUTF8String(buf);
        int count = buf.readInt();
        double dist = buf.readDouble();
        boolean isEntity = buf.readBoolean();
        int entityId = buf.readInt();
        int dimId = buf.readInt();
        return new SearchResultEntry(pos, name, count, dist, isEntity, entityId, dimId);
    }
}
