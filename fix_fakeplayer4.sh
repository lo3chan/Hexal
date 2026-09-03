#!/bin/bash
sed -i 's/ServerboundEntityTagQueryPacket/net.minecraft.network.protocol.game.ServerboundEntityTagQuery/g' Common/src/main/java/ram/talia/hexal/api/fakes/FakePlayer.kt
sed -i 's/ServerboundBlockEntityTagQueryPacket/net.minecraft.network.protocol.game.ServerboundBlockEntityTagQuery/g' Common/src/main/java/ram/talia/hexal/api/fakes/FakePlayer.kt
sed -i '/handleResourcePackResponse/d' Common/src/main/java/ram/talia/hexal/api/fakes/FakePlayer.kt
sed -i '/handleKeepAlive/d' Common/src/main/java/ram/talia/hexal/api/fakes/FakePlayer.kt
sed -i '/handleClientInformation/d' Common/src/main/java/ram/talia/hexal/api/fakes/FakePlayer.kt
sed -i '/handleCustomPayload/d' Common/src/main/java/ram/talia/hexal/api/fakes/FakePlayer.kt
sed -i '/override fun updateOptions(pkt: net.minecraft.network.protocol.common.ServerboundClientInformationPacket)/d' Common/src/main/java/ram/talia/hexal/api/fakes/FakePlayer.kt
