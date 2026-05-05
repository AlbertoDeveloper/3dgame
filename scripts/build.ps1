$ErrorActionPreference = "Stop"

$root = Split-Path -Parent $PSScriptRoot
$sourceDir = Join-Path $root "src\main\java"
$classesDir = Join-Path $root "build\classes"

if (Test-Path $classesDir) {
    Remove-Item -LiteralPath $classesDir -Recurse -Force
}

New-Item -ItemType Directory -Force -Path $classesDir | Out-Null

$sourcesFile = Join-Path $root "build\sources.txt"
Get-ChildItem -Path $sourceDir -Recurse -Filter *.java |
    ForEach-Object { $_.FullName } |
    Set-Content -Path $sourcesFile -Encoding ASCII

$releaseSupported = $false
try {
    $helpText = (& javac --help 2>&1) -join "`n"
    $releaseSupported = $helpText.Contains("--release")
} catch {
    $releaseSupported = $false
}

if ($releaseSupported) {
    & javac --release 8 -d $classesDir "@$sourcesFile"
} else {
    & javac -source 8 -target 8 -d $classesDir "@$sourcesFile"
}

if ($LASTEXITCODE -ne 0) {
    exit $LASTEXITCODE
}

Write-Host "Compiled classes to $classesDir"
