{
  description = "Build scala source and run a shell with genart packages";

  inputs.utils.url = "github:numtide/flake-utils";

  outputs = { self, nixpkgs, utils }: 
    utils.lib.eachDefaultSystem (system:
      let pkgs = nixpkgs.legacyPackages.${system};
      in rec {
        baseInputs = with pkgs; [ jdk gradle cookiecutter];
        shellInputs = with pkgs; [ imagemagick ffmpeg ];

        devShell = pkgs.mkShell {
          buildInputs = baseInputs ++ shellInputs;
          shellHook = ''
            export JAVA_HOME=${pkgs.jdk}
            PATH="${pkgs.jdk}/bin:$PATH"
            '';
        };
      });
}
