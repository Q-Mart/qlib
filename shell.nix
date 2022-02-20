{ pkgs ? import <nixpkgs> {
  overlays = [
    (self: super: {
      jre = super.jdk11;
    })
  ];
} }:
pkgs.mkShell {
  name = "sbt";
  buildInputs = with pkgs; [
    jdk11
    scala
    sbt
  ];
}
