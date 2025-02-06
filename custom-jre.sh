/usr/lib/jvm/java-21-openjdk-amd64/bin/jlink \
    --module-path /home/cmuser/Tools/openjfx-21.0.6_linux-x64_bin-sdk/javafx-sdk-21.0.6/lib \
    --add-modules java.base,java.logging,java.desktop,javafx.controls,javafx.fxml,javafx.graphics,java.net.http,java.sql  \
    --output /home/cmuser/Documents/workspace/vocab/vocab-desktop/fx-custom-runtime
