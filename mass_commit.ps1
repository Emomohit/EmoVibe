Remove-Item -Recurse -Force .git -ErrorAction SilentlyContinue
git init
git config user.name "Mohit"
git config user.email "mohitguna01@gmail.com"
git checkout -b main
git add .gitignore
git commit -m "docs: Initial commit and configuration"

git add .
$allFiles = git diff --name-only --cached
git reset

$chunk = @()
$counter = 1
foreach ($file in $allFiles) {
    $chunk += $file
    if ($chunk.Count -ge 30) {
        $chunk | Out-File -FilePath chunk.txt -Encoding utf8
        git add --pathspec-from-file=chunk.txt
        git commit -m "feat: add core app infrastructure components (batch $counter)"
        $chunk = @()
        $counter++
    }
}
if ($chunk.Count -gt 0) {
    $chunk | Out-File -FilePath chunk.txt -Encoding utf8
    git add --pathspec-from-file=chunk.txt
    git commit -m "feat: add core app infrastructure components (batch $counter)"
}
Remove-Item chunk.txt -ErrorAction SilentlyContinue

git remote add origin https://github.com/Emomohit/EmoVibe.git
git push -u origin main --force
Write-Output "Massive contribution boost completed successfully!"
