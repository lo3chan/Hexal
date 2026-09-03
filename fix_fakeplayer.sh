#!/bin/bash
sed -i 's/ServerboundEntityTagQuery/ServerboundEntityTagQueryPacket/g' Common/src/main/java/ram/talia/hexal/api/fakes/FakePlayer.kt
sed -i 's/ServerboundBlockEntityTagQuery/ServerboundBlockEntityTagQueryPacket/g' Common/src/main/java/ram/talia/hexal/api/fakes/FakePlayer.kt
sed -i 's/ServerboundResourcePackPacket/ServerboundResourcePackResponsePacket/g' Common/src/main/java/ram/talia/hexal/api/fakes/FakePlayer.kt
sed -i 's/override fun handleKeepAlive(packet: ServerboundKeepAlivePacket) {}/override fun handleKeepAlive(packet: net.minecraft.network.protocol.common.ServerboundKeepAlivePacket) {}/g' Common/src/main/java/ram/talia/hexal/api/fakes/FakePlayer.kt
sed -i 's/override fun handleClientInformation(packet: ServerboundClientInformationPacket) {}/override fun handleClientInformation(packet: net.minecraft.network.protocol.common.ServerboundClientInformationPacket) {}/g' Common/src/main/java/ram/talia/hexal/api/fakes/FakePlayer.kt
sed -i 's/override fun updateOptions(pkt: ServerboundClientInformationPacket) { }/override fun updateOptions(pkt: net.minecraft.network.protocol.common.ServerboundClientInformationPacket) { }/g' Common/src/main/java/ram/talia/hexal/api/fakes/FakePlayer.kt
sed -i 's/override fun handleCustomPayload(packet: ServerboundCustomPayloadPacket) {}/override fun handleCustomPayload(packet: net.minecraft.network.protocol.common.ServerboundCustomPayloadPacket) {}/g' Common/src/main/java/ram/talia/hexal/api/fakes/FakePlayer.kt
