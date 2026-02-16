# Script PowerShell para subir BioGuard a GitHub
# Este script configura Git, inicializa el repo y hace push

Write-Host @"
╔════════════════════════════════════════════════════════════════╗
║           SUBIDA DE BIOGUARD A GITHUB                         ║
╚════════════════════════════════════════════════════════════════╝
"@

# Cambiar al directorio del proyecto
$projectPath = "C:\Users\jhona\OneDrive\Escritorio\Backend-2026\ProyectoTCP\BioGuard"
Set-Location $projectPath

Write-Host "📂 Directorio: $projectPath" -ForegroundColor Cyan
Write-Host ""

# Verificar si Git está instalado
Write-Host "🔍 Verificando Git..." -ForegroundColor Yellow
$gitCheck = Get-Command git -ErrorAction SilentlyContinue

if (-not $gitCheck) {
    Write-Host "❌ Error: Git no está instalado" -ForegroundColor Red
    Write-Host ""
    Write-Host "📥 Por favor instala Git desde:" -ForegroundColor Yellow
    Write-Host "   https://git-scm.com/download/win" -ForegroundColor Cyan
    Write-Host ""
    Write-Host "Después de instalar, ejecuta este script nuevamente." -ForegroundColor Yellow
    Read-Host "Presiona Enter para salir"
    exit 1
}

Write-Host "✅ Git está disponible" -ForegroundColor Green
Write-Host ""

# Configurar Git (si es necesario)
Write-Host "⚙️  Configurando Git..." -ForegroundColor Yellow
git config --global user.name "Jhonatan" 2>$null
git config --global user.email "jhonatan@example.com" 2>$null
Write-Host "✅ Configuración completada" -ForegroundColor Green
Write-Host ""

# Verificar si ya existe repositorio Git
if (Test-Path ".git") {
    Write-Host "✅ Repositorio Git ya existe" -ForegroundColor Green
} else {
    Write-Host "🔧 Inicializando repositorio Git..." -ForegroundColor Yellow
    git init
    Write-Host "✅ Repositorio inicializado" -ForegroundColor Green
}

Write-Host ""

# Agregar README.md
Write-Host "📝 Procesando README.md..." -ForegroundColor Yellow
if (Test-Path "README.md") {
    Write-Host "✅ README.md existe" -ForegroundColor Green
} else {
    Write-Host "📄 Creando README.md..." -ForegroundColor Yellow
    "# BioGuard" | Out-File -FilePath "README.md" -Encoding UTF8
}

# Agregar al staging
Write-Host "📌 Agregando archivos al staging..." -ForegroundColor Yellow
git add .

Write-Host "✅ Archivos listos" -ForegroundColor Green
Write-Host ""

# Verificar cambios
Write-Host "📊 Cambios pendientes:" -ForegroundColor Cyan
git status --short
Write-Host ""

# Hacer commit
Write-Host "💾 Realizando commit..." -ForegroundColor Yellow
git commit -m "first commit" 2>$null

if ($LASTEXITCODE -eq 0) {
    Write-Host "✅ Commit realizado" -ForegroundColor Green
} else {
    Write-Host "⚠️  No hay cambios nuevos para hacer commit" -ForegroundColor Yellow
}

Write-Host ""

# Cambiar rama a main
Write-Host "🔀 Configurando rama principal..." -ForegroundColor Yellow
git branch -M main
Write-Host "✅ Rama configurada como 'main'" -ForegroundColor Green
Write-Host ""

# Agregar remoto
Write-Host "🔗 Configurando repositorio remoto..." -ForegroundColor Yellow
git remote remove origin 2>$null
git remote add origin https://github.com/JhonatanTTamayo/BioGuard.git

# Verificar remoto
$remoteCheck = git remote -v 2>$null | Select-String "origin"
if ($remoteCheck) {
    Write-Host "✅ Remoto configurado:" -ForegroundColor Green
    Write-Host "   https://github.com/JhonatanTTamayo/BioGuard.git" -ForegroundColor Cyan
} else {
    Write-Host "❌ Error al configurar remoto" -ForegroundColor Red
    Read-Host "Presiona Enter para salir"
    exit 1
}

Write-Host ""

# Hacer push
Write-Host "📤 Subiendo a GitHub..." -ForegroundColor Yellow
Write-Host "⚠️  Es posible que se solicite autenticación." -ForegroundColor Yellow
Write-Host ""

git push -u origin main 2>&1

if ($LASTEXITCODE -eq 0) {
    Write-Host ""
    Write-Host @"
╔════════════════════════════════════════════════════════════════╗
║  ✅ ¡PROYECTO SUBIDO EXITOSAMENTE A GITHUB!                   ║
╚════════════════════════════════════════════════════════════════╝
"@ -ForegroundColor Green

    Write-Host ""
    Write-Host "📊 Información del repositorio:" -ForegroundColor Cyan
    Write-Host "   URL: https://github.com/JhonatanTTamayo/BioGuard" -ForegroundColor Green
    Write-Host "   Rama: main" -ForegroundColor Green
    Write-Host "   Autor: Jhonatan" -ForegroundColor Green
    Write-Host ""

    Write-Host "📋 Próximos pasos:" -ForegroundColor Yellow
    Write-Host "   1. Verifica el repositorio en GitHub" -ForegroundColor Cyan
    Write-Host "   2. Añade más archivos si es necesario" -ForegroundColor Cyan
    Write-Host "   3. Crea releases/tags para versiones" -ForegroundColor Cyan
    Write-Host ""
} else {
    Write-Host ""
    Write-Host "⚠️  Posible error en el push. Verifica:" -ForegroundColor Yellow
    Write-Host "   • Tu conexión a internet" -ForegroundColor Cyan
    Write-Host "   • Tus credenciales de GitHub" -ForegroundColor Cyan
    Write-Host "   • Los permisos del repositorio" -ForegroundColor Cyan
    Write-Host ""
}

Read-Host "Presiona Enter para cerrar"

