$allFiles = git ls-files --others --exclude-standard
$chunkSize = 30
for ($i = 0; $i -lt $allFiles.Count; $i += $chunkSize) {
    $chunk = $allFiles[$i..($i + $chunkSize - 1)] | Where-Object { $_ -ne $null }
    [System.IO.File]::WriteAllLines((Join-Path (Get-Location) "chunk.txt"), $chunk, [System.Text.Encoding]::UTF8)
    git add --pathspec-from-file=chunk.txt
    git commit -m "feat: add app infrastructure components (batch $(($i/$chunkSize)+1))"
}
git push origin main
Write-Output "Push completed!"
