$ErrorActionPreference = "Stop"

$root = Split-Path -Parent $PSScriptRoot
& (Join-Path $PSScriptRoot "build.ps1")
& java -cp (Join-Path $root "build\classes") com.retro3d.Main
