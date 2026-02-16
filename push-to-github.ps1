# Script PowerShell para subir BioGuard a GitHub
# Usuario: JhonatanTTamayo
# Proyecto: BioGuard - Genomic Surveillance System

$projectPath = "C:\Users\jhona\OneDrive\Escritorio\Backend-2026\ProyectoTCP\BioGuard"
$githubUser = "JhonatanTTamayo"
$repoName = "BioGuard"
$repoUrl = "https://github.com/$githubUser/$repoName.git"

Write-Host @"
╔════════════════════════════════════════════════════════════════╗
║           BIOGUARD - GITHUB UPLOAD SCRIPT                     ║
║           Usuario: $githubUser                      ║
╚════════════════════════════════════════════════════════════════╝
"@ -ForegroundColor Cyan

Set-Location $projectPath
Write-Host "`n📂 Proyecto: $projectPath`n" -ForegroundColor Yellow

# Verificar Git
Write-Host "🔍 Verificando Git..." -ForegroundColor Yellow
$gitCheck = Get-Command git -ErrorAction SilentlyContinue

if (-not $gitCheck) {
    Write-Host "❌ Git no está instalado en el PATH" -ForegroundColor Red
    Write-Host "`nDescarga Git desde: https://git-scm.com/download/win" -ForegroundColor Cyan
    exit 1
}

Write-Host "✅ Git encontrado`n" -ForegroundColor Green

# Configurar Git
Write-Host "⚙️  Configurando credenciales de Git..." -ForegroundColor Yellow
git config --global user.name "Jhonatan Tamayo" 2>$null
git config --global user.email "jhonatan.tamayo@example.com" 2>$null
Write-Host "✅ Credenciales configuradas`n" -ForegroundColor Green

# Inicializar o verificar repo
if (Test-Path ".git") {
    Write-Host "✅ Repositorio Git ya existe`n" -ForegroundColor Green
} else {
    Write-Host "🔧 Inicializando repositorio Git..." -ForegroundColor Yellow
    git init
    Write-Host "✅ Repositorio inicializado`n" -ForegroundColor Green
}

# Agregar README.md
Write-Host "📝 Actualizando README.md..." -ForegroundColor Yellow
if (-not (Test-Path "README.md")) {
    "# BioGuard" | Out-File -FilePath "README.md" -Encoding UTF8
    Write-Host "✅ README.md creado`n" -ForegroundColor Green
} else {
    Write-Host "✅ README.md existe`n" -ForegroundColor Green
}

# Agregar todos los archivos
Write-Host "📌 Agregando archivos al staging..." -ForegroundColor Yellow
git add .
Write-Host "✅ Archivos agregados`n" -ForegroundColor Green

# Mostrar estado
Write-Host "📊 Estado del repositorio:" -ForegroundColor Cyan
git status --short
Write-Host ""

# Commit
Write-Host "💾 Realizando commit..." -ForegroundColor Yellow
git commit -m "first commit" 2>$null
if ($LASTEXITCODE -eq 0) {
    Write-Host "✅ Commit realizado`n" -ForegroundColor Green
} else {
    Write-Host "⚠️  No hay cambios nuevos para hacer commit`n" -ForegroundColor Yellow
}

# Rama main
Write-Host "🔀 Configurando rama a main..." -ForegroundColor Yellow
git branch -M main
Write-Host "✅ Rama configurada`n" -ForegroundColor Green

# Remoto
Write-Host "🔗 Configurando repositorio remoto..." -ForegroundColor Yellow
git remote remove origin 2>$null
git remote add origin $repoUrl
Write-Host "✅ Remoto: $repoUrl`n" -ForegroundColor Green

# Push
Write-Host "📤 Subiendo a GitHub..." -ForegroundColor Yellow
Write-Host "⚠️  Se puede solicitar autenticación`n" -ForegroundColor Yellow

git push -u origin main

if ($LASTEXITCODE -eq 0) {
    Write-Host @"
╔════════════════════════════════════════════════════════════════╗
║  ✅ ¡PROYECTO SUBIDO EXITOSAMENTE!                           ║
╚════════════════════════════════════════════════════════════════╝

📊 DETALLES:
   👤 Usuario: $githubUser
   📦 Repositorio: $repoName
   🔗 URL: $repoUrl
   🌳 Rama: main
   ✨ Versión: 1.0.0

🎉 ¡LISTO! Accede a tu repositorio en GitHub.

"@ -ForegroundColor Green
} else {
    Write-Host @"
⚠️  ERROR EN EL PUSH

Verifica:
   • Conexión a internet
   • Credenciales de GitHub
   • Permisos del repositorio
   • Token de acceso (si usas autenticación de dos factores)

"@ -ForegroundColor Red
}

Read-Host "Presiona Enter para cerrar"

