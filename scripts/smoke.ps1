$ErrorActionPreference = "Stop"

$root = Split-Path -Parent $PSScriptRoot
& (Join-Path $PSScriptRoot "build.ps1")
if ($LASTEXITCODE -ne 0) {
    exit $LASTEXITCODE
}

$testClassesDir = Join-Path $root "build\test-classes"
if (Test-Path $testClassesDir) {
    Remove-Item -LiteralPath $testClassesDir -Recurse -Force
}
New-Item -ItemType Directory -Force -Path $testClassesDir | Out-Null

$testSourcesFile = Join-Path $root "build\test-sources.txt"
$mainSourceDir = Join-Path $root "src\main\java"
$testSourceDir = Join-Path $root "src\test\java"
@(Get-ChildItem -Path $mainSourceDir -Recurse -Filter *.java) +
    @(Get-ChildItem -Path $testSourceDir -Recurse -Filter *.java) |
    ForEach-Object { $_.FullName } |
    Set-Content -Path $testSourcesFile -Encoding ASCII

$releaseSupported = $false
try {
    $helpText = (& javac --help 2>&1) -join "`n"
    $releaseSupported = $helpText.Contains("--release")
} catch {
    $releaseSupported = $false
}

if ($releaseSupported) {
    & javac --release 8 -d $testClassesDir "@$testSourcesFile"
} else {
    & javac -source 8 -target 8 -d $testClassesDir "@$testSourcesFile"
}

if ($LASTEXITCODE -ne 0) {
    exit $LASTEXITCODE
}

& java -cp "$testClassesDir;$(Join-Path $root "build\classes")" com.retro3d.tools.RenderSmokeTest
if ($LASTEXITCODE -ne 0) {
    exit $LASTEXITCODE
}
