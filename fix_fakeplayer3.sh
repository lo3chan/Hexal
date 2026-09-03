#!/bin/bash
sed -i 's/ServerPlayer(level.server, level, name)/ServerPlayer(level.server, level, name, net.minecraft.server.level.ClientInformation.createDefault())/g' Common/src/main/java/ram/talia/hexal/api/fakes/FakePlayer.kt
sed -i 's/ServerGamePacketListenerImpl(server, DUMMY_CONNECTION, player)/ServerGamePacketListenerImpl(server, DUMMY_CONNECTION, player, net.minecraft.server.network.CommonListenerCookie.createInitial(player.gameProfile))/g' Common/src/main/java/ram/talia/hexal/api/fakes/FakePlayer.kt
