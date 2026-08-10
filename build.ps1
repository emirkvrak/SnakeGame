$ErrorActionPreference = 'Stop'

$jdkHome = $env:JAVA_HOME
if (-not $jdkHome) { throw 'JAVA_HOME tanımlı değil. JDK 21 kurun veya JAVA_HOME ayarlayın.' }

$sourceFiles = Get-ChildItem -Path 'src\main\java' -Recurse -Filter '*.java' | ForEach-Object FullName
New-Item -ItemType Directory -Force -Path 'target\classes' | Out-Null
& "$jdkHome\bin\javac.exe" -encoding UTF-8 -d 'target\classes' $sourceFiles
Copy-Item -Path 'src\main\resources\*' -Destination 'target\classes' -Force
Write-Host 'Derleme tamamlandı: target/classes'
