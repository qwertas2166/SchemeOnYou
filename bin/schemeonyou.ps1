$ErrorActionPreference = 'Stop'
$appHome = Resolve-Path (Join-Path $PSScriptRoot '..')
$javaHome = if ($env:JAVA_HOME) { $env:JAVA_HOME } else { Join-Path $HOME 'workspace/java/openjdk-25.0.2' }
$java = Join-Path $javaHome 'bin/java.exe'
$entries = @(
    (Join-Path $appHome 'client/target/classes'),
    (Join-Path $appHome 'core/target/classes'),
    (Join-Path $appHome 'client/target/client-0.0.1-SNAPSHOT.jar'),
    (Join-Path $appHome 'core/target/core-0.0.1-SNAPSHOT.jar')
)
foreach ($libDir in @((Join-Path $appHome 'client/target/lib'), (Join-Path $appHome 'core/target/lib'))) {
    if (Test-Path $libDir) {
        $entries += Get-ChildItem -Path $libDir -Filter '*.jar' | ForEach-Object { $_.FullName }
    }
}
& $java -cp ($entries -join [IO.Path]::PathSeparator) see.schemeonyou.SchemeOnYouMain @args
exit $LASTEXITCODE
