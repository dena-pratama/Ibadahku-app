package com.ibadahku.app.data.model

enum class OemBrand(val displayName: String, val guideTitle: String, val guideInstructions: String) {
    XIAOMI(
        "Xiaomi / Redmi / POCO",
        "Panduan Autostart & Baterai MIUI/HyperOS",
        "1. Aktifkan 'Autostart' di Info Aplikasi Ibadahku.\n2. Buka Penghemat Baterai -> Pilih 'Tidak ada batasan' (No restrictions).\n3. Kunci Ibadahku di daftar Recent Apps dengan ikon gembok."
    ),
    OPPO_REALME(
        "OPPO / Realme (ColorOS / Realme UI)",
        "Panduan Auto-launch & Startup",
        "1. Pengaturan -> Manajemen Aplikasi -> Ibadahku -> Izinkan Startup Otomatis.\n2. Baterai -> Izinkan aktivitas latar belakang tanpa batas.\n3. Kunci aplikasi di daftar tugas baru."
    ),
    VIVO(
        "Vivo / iQOO (Funtouch OS / OriginOS)",
        "Panduan Konsumsi Daya Latar Belakang",
        "1. Pengaturan -> Baterai -> Manajemen konsumsi daya latar belakang.\n2. Temukan Ibadahku -> Pilih 'Izinkan konsumsi daya latar belakang tinggi'.\n3. Pengelola Izin -> Autostart -> Izinkan Ibadahku."
    ),
    SAMSUNG(
        "Samsung (One UI)",
        "Panduan Sleeping Apps Samsung",
        "1. Pengaturan -> Perawatan Perangkat -> Baterai -> Batas penggunaan latar belakang.\n2. Pastikan Ibadahku BUKAN di 'Sleeping apps' maupun 'Deep sleeping apps'.\n3. Tambahkan Ibadahku ke 'Aplikasi yang tidak pernah tidur' (Never sleeping apps)."
    ),
    TRANSSION(
        "Infinix / Tecno / itel (XOS / HiOS)",
        "Panduan Phone Master & Auto-start",
        "1. Buka Phone Master -> Auto-start management -> Izinkan Ibadahku.\n2. Baterai -> Penghemat daya -> Matikan optimasi baterai untuk Ibadahku."
    ),
    GENERIC(
        "Android Standar / Google Pixel",
        "Panduan Penghemat Baterai & Notifikasi",
        "1. Buka Info Aplikasi -> Baterai -> Pilih 'Tidak Dibatasi' (Unrestricted).\n2. Pastikan Izin Alarm Tepat Waktu (Exact Alarm) diaktifkan.\n3. Izinkan Notifikasi kategori Alarm."
    )
}

data class ReliabilityStatus(
    val notificationPermissionGranted: Boolean = true,
    val exactAlarmPermissionGranted: Boolean = true,
    val batteryOptimizationIgnored: Boolean = false,
    val dndOverrideStatus: String = "Normal",
    val alarmVolumeLevel: Int = 100,
    val oemBrand: OemBrand = OemBrand.GENERIC
)
