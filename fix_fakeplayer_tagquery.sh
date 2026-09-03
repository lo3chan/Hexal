#!/bin/bash
sed -i '/handleEntityTagQuery/d' Common/src/main/java/ram/talia/hexal/api/fakes/FakePlayer.kt
sed -i '/handleBlockEntityTagQuery/d' Common/src/main/java/ram/talia/hexal/api/fakes/FakePlayer.kt
