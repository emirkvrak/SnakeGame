$ErrorActionPreference = 'Stop'
& "$PSScriptRoot\build.ps1"

New-Item -ItemType Directory -Force -Path 'target\app' | Out-Null
& "$env:JAVA_HOME\bin\jar.exe" --create --file 'target\app\SnakeGame.jar' --main-class yilanoyunu.YilanOyunu -C 'target\classes' .

New-Item -ItemType Directory -Force -Path 'dist' | Out-Null
if (Test-Path 'dist\SnakeGame') { Remove-Item -Recurse -Force 'dist\SnakeGame' }
& "$env:JAVA_HOME\bin\jpackage.exe" --type app-image --name SnakeGame --app-version 1.0.0 --input 'target\app' --main-jar SnakeGame.jar --main-class yilanoyunu.YilanOyunu --dest 'dist'
Copy-Item -LiteralPath 'target\app\SnakeGame.jar' -Destination 'dist\SnakeGame.jar' -Force
if (Test-Path 'dist\SnakeGame-portable.zip') { Remove-Item -Force 'dist\SnakeGame-portable.zip' }
Compress-Archive -Path 'dist\SnakeGame' -DestinationPath 'dist\SnakeGame-portable.zip'

Write-Host 'Java gerektirmeyen paket hazırlandı: dist/SnakeGame'
Write-Host 'Taşınabilir ZIP hazırlandı: dist/SnakeGame-portable.zip'
Write-Host 'Geliştirici JAR dosyası hazırlandı: dist/SnakeGame.jar'
