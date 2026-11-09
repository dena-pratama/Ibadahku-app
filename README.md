# 🌙 Ibadahku (إِبَادَتِي) - Aplikasi Pendamping Ibadah Harian Muslim Android

[![Kotlin](https://img.shields.io/badge/Kotlin-2.4.21-purple.svg?style=flat&logo=kotlin)](https://kotlinlang.org)
[![Android Gradle Plugin](https://img.shields.io/badge/AGP-9.4.1-green.svg?style=flat&logo=android)](https://developer.android.com/studio/releases/gradle-plugin)
[![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-BOM%202026.09.00-blue.svg?style=flat&logo=jetpackcompose)](https://developer.android.com/jetpack/compose)
[![Material 3](https://img.shields.io/badge/Material%203-Enabled-emerald.svg?style=flat)](https://m3.material.io)
[![Min SDK](https://img.shields.io/badge/Min%20SDK-26%20(Android%208.0)-orange.svg?style=flat)](https://developer.android.com)
[![Target SDK](https://img.shields.io/badge/Target%20SDK-36%20%7C%20Compile%2037-brightgreen.svg?style=flat)](https://developer.android.com)
[![Offline First](https://img.shields.io/badge/Privacy-100%25%20Offline%20First-success.svg?style=flat)](https://github.com)
[![License](https://img.shields.io/badge/License-MIT-lightgrey.svg?style=flat)](LICENSE)

**Ibadahku** adalah aplikasi Android *offline-first* modern yang dirancang khusus untuk membantu umat Muslim menjaga konsistensi, kekhusyukan, dan kedisiplinan ibadah harian. Dibangun menggunakan arsitektur modern **Clean Architecture + MVVM**, **100% Jetpack Compose**, serta mengusung filosofi **Zero-Tracking, Tanpa Iklan, dan Privasi Mutlak** (aplikasi tidak memerlukan izin internet sama sekali).

---

## 📑 Daftar Isi

- [Prinsip Utama & Keunggulan](#-prinsip-utama--keunggulan)
- [Fitur Utama](#-fitur-utama)
  - [1. Kartu Waktu Sholat Interaktif & Animasi Masjid](#1-kartu-waktu-sholat-interaktif--animasi-masjid)
  - [2. Pengingat Sholat 5 Waktu & Dering Adzan Super Andal](#2-pengingat-sholat-5-waktu--dering-adzan-super-andal)
  - [3. Kustomisasi Suara Adzan (Bawaan & File Lokal)](#3-kustomisasi-suara-adzan-bawaan--file-lokal)
  - [4. Riwayat & Statistik Konsistensi Ibadah](#4-riwayat--statistik-konsistensi-ibadah)
  - [5. Pelacak Qadha Sholat Fardhu](#5-pelacak-qadha-sholat-fardhu)
  - [6. Tadarus Al-Qur'an & Mushaf Reader Offline](#6-tadarus-al-quran--mushaf-reader-offline)
  - [7. To-Do Ibadah & Tasbih Digital](#7-to-do-ibadah--tasbih-digital)
  - [8. Kompas Presisi Arah Kiblat](#8-kompas-presisi-arah-kiblat)
  - [9. Tema Aplikasi Dinamis (Light, Dark, System)](#9-tema-aplikasi-dinamis-light-dark-system)
  - [10. Cek Keandalan Pengingat & Panduan OEM](#10-cek-keandalan-pengingat--panduan-oem)
  - [11. Profil Pengguna & Sapaan Dinamis](#11-profil-pengguna--sapaan-dinamis)
- [Tech Stack & Pustaka](#-tech-stack--pustaka)
- [Struktur Proyek](#-struktur-proyek)
- [Panduan Instalasi & Menjalankan](#-panduan-instalasi--menjalankan)
  - [Prasyarat Sistem](#prasyarat-sistem)
  - [Langkah 1: Kloning Repositori](#langkah-1-kloning-repositori)
  - [Langkah 2: Konfigurasi Environment (Java & Android SDK)](#langkah-2-konfigurasi-environment-java--android-sdk)
  - [Langkah 3: Membuka Proyek di Android Studio](#langkah-3-membuka-proyek-di-android-studio)
  - [Langkah 4: Kompilasi & Build APK (CLI)](#langkah-4-kompilasi--build-apk-cli)
  - [Langkah 5: Menjalankan di Emulator / Perangkat Fisik](#langkah-5-menjalankan-di-emulator--perangkat-fisik)
- [Pengujian Unit (Unit Testing)](#-pengujian-unit-unit-testing)
- [Privasi & Keamanan](#-privasi--keamanan)
- [Kontribusi & Lisensi](#-kontribusi--lisensi)

---

## 🌟 Prinsip Utama & Keunggulan

1. **100% Offline-First:**  
   Tidak ada izin `android.permission.INTERNET` di dalam `AndroidManifest.xml`. Seluruh perhitungan sholat, pencarian surah Al-Qur'an, kalkulasi bearing kiblat, dan penyimpanan data ibadah berjalan lokal di perangkat pengguna.
2. **Kebal Terhadap Mode Doze (Super Andal):**  
   Memanfaatkan `AlarmManager.setAlarmClock()` dan `setExactAndAllowWhileIdle()` yang dipadukan dengan Foreground Service beraliran `AudioAttributes.USAGE_ALARM`, menjamin suara adzan tetap berkumandang tepat waktu meskipun HP dalam keadaan terkunci, layar mati, atau mode hemat daya agresif.
3. **Kenyamanan Visual (Light & Dark Mode Penuh):**  
   Desain visual bernuansa *Islamic Sage Green* dan *Deep Slate*, dirancang ramah di mata untuk penggunaan siang hari maupun malam hari menjelang Subuh.
4. **Respon Taktil (Haptic Feedback):**  
   Integrasi getaran halus pada tasbih digital dan kompas kiblat ketika perangkat tepat menghadap Ka'bah (toleransi presisi ±3°).

---

## 🚀 Fitur Utama

### 1. Kartu Waktu Sholat Interaktif & Animasi Masjid
- **Gradien Langit Adaptif:** Latar belakang kartu masjid secara dinamis berubah sesuai fase waktu sholat aktif:
  - *Subuh:* Gradien fajar keemasan lembut.
  - *Dzuhur:* Biru cerah terik siang hari.
  - *Ashar:* Oranye keemasan teduh sore hari.
  - *Maghrib:* Lembayung ungu senja matahari terbenam.
  - *Isya:* Langit malam pekat bertabur bintang berkelap-kelip.
- **Elemen Grafis Canvas:** Awan melayang berulang, bintang yang berkedip secara acak (*alpha twinkling*), matahari/bulan bercahaya, serta siluet kubah masjid dan menara dengan lentera puncak yang menyala.
- **Selector Pill Waktu Sholat:** Pilihan cepat *Subuh, Dzuhur, Ashar, Maghrib, Isya* untuk meninjau jam dan keutamaan sholat berikutnya.
- **Tombol Cepat Notifikasi:** Ikon lonceng interaktif untuk menyalakan/mematikan dering adzan langsung dari kartu beranda.

### 2. Pengingat Sholat 5 Waktu & Dering Adzan Super Andal
- **Perhitungan Astronomis Presisi:**
  - Standar Kementerian Agama Republik Indonesia (Kemenag RI) dengan sudut Subuh 20° dan Isya 18°.
  - Opsi otoritas internasional: Muslim World League (MWL), Egyptian General Authority, Umm al-Qura (Makkah), dan University of Islamic Sciences Karachi.
  - Pilihan perhitungan Ashar berdasarkan Madhab Syafi'i/Hambali/Maliki (rasio bayangan 1:1) atau Madhab Hanafi (rasio bayangan 2:1).
  - Penyesuaian manual koreksi menit per waktu sholat (misal: +2 menit waktu ihtiyat).
- **Notifikasi Aksi:** Notifikasi kepala (*Heads-up Notification*) saat waktu masuk sholat dengan opsi:
  - `Sudah Sholat`: Langsung mencatat ke riwayat harian.
  - `Snooze 10 Menit`: Menunda pengingat sementara.
  - `Hentikan Audio`: Mematikan lantunan adzan.
- **Boot & Timezone Receiver:** Otomatis menjadwalkan ulang seluruh alarm 48 jam ke depan saat perangkat selesai dihidupkan ulang (*reboot*) atau saat pengguna berpindah zona waktu.

### 3. Kustomisasi Suara Adzan (Bawaan & File Lokal)
- **Tab Suara Bawaan:**
  - *Masjid Nabawi (Madinah)*: Rekaman adzan merdu khas Kota Madinah.
  - *Adzan Subuh (Tatswib)*: Rekaman adzan dengan seruan *"Ash-shalatu khairum minan naum"*.
  - *Nada Dering / Bip Singkat*: Notifikasi ringkas bagi pengguna yang membutuhkan pengingat hening/singkat di kantor.
  - Tombol uji dengar audio (*Play/Stop*) langsung di dalam dialog.
- **Tab Pilih di File (Storage Access Framework - SAF):**
  - Memungkinkan pengguna memilih berkas adzan kustom (`.mp3`, `.wav`, `.aac`) dari memori internal atau kartu SD.
  - Mengamankan izin baca persisten via `takePersistableUriPermission`.
  - Preview suara kustom langsung sebelum disimpan.

### 4. Riwayat & Statistik Konsistensi Ibadah
- **Segmented Tab Switcher:**
  - **Tab Jadwal Hari Ini:** Menampilkan kartu sholat 5 waktu dengan jam tepat, countdown mundur, badge status sholat (*Sedang Berjalan / Terlewat / Selesai*), dan tombol centang manual.
  - **Tab Riwayat & Statistik:** Grafik batang (*Bar Chart*) konsistensi sholat 7 hari terakhir (Senin s.d. Minggu) dilengkapi badge evaluasi (misal: *"88% Sangat Baik"*).
- **Ringkasan Terpadu:** Menampilkan progres Tadarus Qur'an hari ini, jumlah to-do ibadah tuntas, serta saldo utang qadha sholat.

### 5. Pelacak Qadha Sholat Fardhu
- **Pencatat Utang Sholat Otomatis & Terstruktur:**
  - Melacak utang sholat untuk masing-masing 5 waktu sholat fardhu (Subuh, Dzuhur, Ashar, Maghrib, Isya).
  - Kartu total akumulasi utang sholat dengan estimasi target hari pelunasan.
  - Tombol tambah `[+]` dan bayar/kurang `[-]` dengan getaran haptik responsif.
  - **Dialog Selebrasi:** Pop-up apresiasi ketika seluruh utang sholat berhasil dilunasi.
  - **Dialog Reset:** Opsi mengosongkan kembali catatan utang sholat fardhu dengan konfirmasi aman.
  - **Motivasi Hadits:** Hadits shahih Rasulullah ﷺ mengenai kewajiban mengqadha sholat yang terlupa/tertidur (HR. Bukhari no. 597 & Muslim no. 684).

### 6. Tadarus Al-Qur'an & Mushaf Reader Offline
- **Indikator Cincin Progres Khatam:**
  - Pelacak progres halaman mushaf standar Madinah (604 halaman).
  - Pilihan rencana target khatam fleksibel (30 hari, 60 hari, 90 hari, 180 hari, hingga 365 hari) dengan penghitungan otomatis target halaman harian.
- **Mushaf Reader Lengkap:**
  - Teks Arab Rasm Utsmani yang jelas dan mudah dibaca.
  - Transliterasi Latin perkata/perayat.
  - Terjemahan resmi Kementerian Agama RI dalam Bahasa Indonesia.
  - Tombol ubah ukuran font Arab secara instan (20sp hingga 36sp).
  - Tombol sakelar tampil/sembunyi terjemahan (*Toggle Translation*).
- **Direktori Surah:**
  - Daftar 114 Surah lengkap dengan nomor, nama Arab, nama Latin, arti surah, dan jumlah ayat.
  - Filter cepat klasifikasi surah (*Makkiyyah* vs *Madaniyyah*).
  - Pencarian nomor dan nama surah seketika (*instant search*).
- **Bookmark & Riwayat Terakhir:** Menyimpan posisi surah dan ayat terakhir yang dibaca secara otomatis.

### 7. To-Do Ibadah & Tasbih Digital
- **Daftar Amalan Harian & Mingguan:**
  - Amalan sunnah siap pakai: Baca Al-Qur'an, Dzikir Pagi, Dzikir Petang, Sholawat Nabi, Sedekah Subuh, Sholat Dhuha, Sholat Tahajud, Puasa Sunnah Senin-Kamis.
  - Template tambah amalan cepat dengan satu ketukan (*One-tap presets*).
  - Filter: *Hari Ini*, *Minggu Ini*, dan *Semua*.
- **Tasbih Digital Interaktif:**
  - **Permukaan Ketuk Lebar (*Tap Anywhere*):** Sentuh di mana saja pada layar untuk menambah hitungan tanpa perlu melihat tombol kecil.
  - **Umpan Balik Haptik Cerdas:** Getaran halus pada setiap ketukan dan getaran panjang saat target dzikir (misal: 33x, 100x) tercapai.
  - **Pustaka Dzikir & Doa:** Dilengkapi bacaan Arab, latin, terjemahan, dan keutamaan fadhilah dzikir.
  - Tombol *Urungkan (Undo)* dan *Reset* hitungan.

### 8. Kompas Presisi Arah Kiblat
- **Kalkulasi Astronomis Great-Circle:**
  - Menghitung azimuth sudut Ka'bah di Makkah (21.4225° LU, 39.8262° BT) dari koordinat lintang & bujur GPS perangkat.
- **Fusi Sensor:**
  - Menggunakan sensor `TYPE_ROTATION_VECTOR` dengan *low-pass damping filter* untuk pergerakan jarum kompas yang halus tanpa goyangan (*jitter-free*).
  - Tampilan visual mata angin, piringan kompas instrumen, dan model 3D Ka'bah dengan Kiswah emas.
  - **Getaran Kunci Kiblat:** Menghasilkan getaran haptik dan lingkaran hijau bercahaya saat perangkat tepat mengarah ke Ka'bah (toleransi presisi ±3°).
  - Dilengkapi slider simulator derajat kompas untuk pengujian di emulator Android.

### 9. Tema Aplikasi Dinamis (Light, Dark, System)
- **Mode Terang (*Light Mode*):**
  - Palet latar *Cream* lembut (`#FBFBF9`), kartu putih bersih, dan teks kontras tinggi *Dark Forest* (`#1B2A22`).
- **Mode Gelap (*Dark Mode*):**
  - Palet latar *Deep Islamic Slate* (`#121B16`), kartu bergradasi *Dark Emerald* (`#1A2620`), dan teks moonlight (`#EFF5F1`) yang sangat nyaman saat malam hari.
- **Ikuti Sistem (*System Default*):**
  - Berpindah secara otomatis mengikuti jadwal mode gelap sistem Android perangkat pengguna.
- **Sinkronisasi Bar Sistem:** Warna Status Bar dan Navigation Bar Android otomatis mengikuti tema yang aktif via `WindowCompat.getInsetsController`.

### 10. Cek Keandalan Pengingat & Panduan OEM
- **Pemeriksaan Izin Sistem:**
  - Status izin notifikasi Android 13+ (`POST_NOTIFICATIONS`).
  - Status izin alarm tepat waktu Android 12+ (`SCHEDULE_EXACT_ALARM`).
  - Status pengecualian optimasi baterai (`ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS`).
- **Panduan Spesifik Pabrikan Perangkat (OEM Bypass):**
  - Panduan langkah demi langkah untuk mengunci aplikasi di latar belakang dan mengaktifkan *Autostart* pada perangkat:
    - **Xiaomi / Redmi / POCO (MIUI / HyperOS)**
    - **OPPO / Realme (ColorOS / Realme UI)**
    - **Vivo / iQOO (Funtouch OS / OriginOS)**
    - **Samsung (One UI)**
    - **Huawei / Honor (EMUI / MagicOS)**
- **Tombol Tes Audio 5 Detik:** Memutar suara alarm uji coba pada aliran audio `USAGE_ALARM` untuk memastikan speaker membunyikan adzan meskipun HP dalam profil *Do Not Disturb (DND)* prioritas.

### 11. Profil Pengguna & Sapaan Dinamis
- Pengguna dapat mengatur nama panggilan sendiri (contoh: *Fulan*, *Ahmad*, *Aisyah*).
- Menampilkan sapaan islami dinamis di Beranda:  
  *"Assalamu'alaikum 👋, Selamat pagi/siang/sore/malam, [Nama]!"*
- Dilengkapi kotak pratinjau (*live preview*) langsung saat mengetik nama di dialog pengaturan.

---

## 🛠 Tech Stack & Pustaka

| Kategori | Teknologi / Pustaka | Versi | Deskripsi |
| :--- | :--- | :--- | :--- |
| **Bahasa** | Kotlin | `2.4.21` | Bahasa pemrograman utama dengan coroutines & StateFlow |
| **Compiler & JDK** | OpenJDK / Android Studio JBR | `21` | Java 21 LTS runtime |
| **Build System** | Gradle & Android Gradle Plugin | AGP `9.4.1` / Gradle `9.8.2` | Sistem otomasi build aplikasi Android |
| **Target OS** | Android SDK | Min `26` (Android 8.0) / Target `36` / Compile `37` | Kompatibilitas luas ke 95%+ perangkat Android aktif |
| **UI Toolkit** | Jetpack Compose | BOM `2026.09.00` | Toolkit deklaratif modern untuk antarmuka pengguna Android |
| **Design System** | Material Design 3 | `1.4.0` / Compose M3 | Komponen Material 3, dynamic color & theming |
| **Navigasi** | Jetpack Navigation Compose | `2.10.2` | Single-Activity declarative navigation |
| **Icons** | Material Icons Extended | Jetpack Compose | Kumpulan ikon material lengkap untuk ibadah |
| **Sensor API** | Android Hardware Sensors | Android Framework | `SensorManager`, `Sensor.TYPE_ROTATION_VECTOR` |
| **Audio Engine** | Android MediaPlayer | Android Framework | Stream audio `AudioAttributes.USAGE_ALARM` |
| **Storage API** | Storage Access Framework (SAF) | Android Framework | `ActivityResultContracts.GetContent` (`audio/*`) |
| **Penyimpanan Lokal** | SharedPreferences & In-Memory | Android Framework | Penyimpanan persisten lokal offline 100% |
| **Unit Testing** | JUnit 4 | `4.13.2` | Pengujian unit domain logika astronomis & kalkulator |

---

## 📁 Struktur Proyek

```text
Ibadahku-app/
├── app/
│   ├── build.gradle.kts                # Konfigurasi dependensi modul aplikasi
│   ├── proguard-rules.pro              # Konfigurasi proteksi & optimasi R8/Proguard
│   └── src/
│       ├── main/
│       │   ├── AndroidManifest.xml     # Deklarasi komponen, permissions & service
│       │   ├── java/com/ibadahku/app/
│       │   │   ├── IbadahkuApp.kt                  # Inisialisasi Aplikasi & Notification Channel
│       │   │   ├── MainActivity.kt                 # Single Activity, NavHost, & Bottom Bar
│       │   │   ├── data/
│       │   │   │   ├── model/
│       │   │   │   │   ├── PrayerTime.kt           # Model jadwal sholat, tipe sholat, & status
│       │   │   │   │   ├── PrayerSettings.kt       # Pengaturan nama, suara adzan, koordinat, dsb.
│       │   │   │   │   ├── QuranModels.kt          # Model surah, ayat, bookmark, & target khatam
│       │   │   │   │   ├── TodoTask.kt             # Model amalan harian, kategori, & tasbih
│       │   │   │   │   └── ReliabilityStatus.kt    # Data izin keandalan & panduan OEM
│       │   │   │   └── repository/
│       │   │   │       ├── AppPreferencesRepository.kt # Penyimpanan persisten preferensi & status
│       │   │   │       ├── QuranRepository.kt      # Konten mushaf Al-Qur'an offline & bookmark
│       │   │   │       └── TodoRepository.kt       # Manajemen tugas amalan & counter tasbih
│       │   │   ├── domain/
│       │   │   │   ├── PrayerCalculator.kt         # Kalkulator astronomis jadwal sholat & Hijriah
│       │   │   │   └── QiblaCalculator.kt          # Formula trigonometri Great-Circle arah Ka'bah
│       │   │   ├── engine/
│       │   │   │   ├── PrayerAlarmScheduler.kt     # Penjadwal exact alarm via AlarmManager
│       │   │   │   ├── AudioPlayerManager.kt       # Pengelola audio adzan (bawaan & file SAF)
│       │   │   │   └── CompassSensorManager.kt     # Filter sensor kompas & deteksi sejajar ±3°
│       │   │   ├── receiver/
│       │   │   │   ├── PrayerAlarmReceiver.kt      # BroadcastReceiver saat alarm sholat berbunyi
│       │   │   │   └── BootReceiver.kt             # Reschedule alarm saat HP restart / ganti jam
│       │   │   ├── service/
│       │   │   │   └── PrayerAlarmService.kt       # Foreground service pemutar adzan di background
│       │   │   └── ui/
│       │   │       ├── theme/
│       │   │       │   ├── Color.kt                # Palet warna Islami (Sage Green, Cream, Dark Slate)
│       │   │       │   ├── Theme.kt                # Konfigurasi Light/Dark MaterialTheme M3
│       │   │       │   └── Type.kt                 # Tipografi & font styling
│       │   │       ├── components/
│       │   │       │   ├── AnimatedMosquePrayerCard.kt # Kartu masjid animasi & gradien langit waktu
│       │   │       │   ├── CompassDialView.kt      # Piringan kompas kiblat & Ka'bah 3D
│       │   │       │   ├── CircularProgressRing.kt # Cincin progres melingkar kustom
│       │   │       │   ├── CompletionDialog.kt     # Dialog pop-up "Alhamdulillah! Amalan tercatat"
│       │   │       │   ├── AddTaskDialog.kt        # Dialog tambah amalan baru
│       │   │       │   └── MosqueArtwork.kt        # Vektor ornamen masjid & bulan sabit
│       │   │       └── screens/
│       │   │           ├── SplashScreen.kt         # Layar sambutan & identitas aplikasi
│       │   │           ├── HomeScreen.kt           # Layar Beranda utama & kartu ringkasan
│       │   │           ├── PrayerScheduleScreen.kt # Layar Jadwal Sholat & Statistik Mingguan
│       │   │           ├── TodoScreen.kt           # Layar To-Do amalan harian & checklist
│       │   │           ├── TasbihScreen.kt         # Layar Tasbih Digital berlayar sentuh lebar
│       │   │           ├── QadhaTrackerScreen.kt   # Layar Pelacak Utang Sholat Fardhu
│       │   │           ├── QiblaScreen.kt          # Layar Kompas Arah Kiblat presisi
│       │   │           ├── TadarusScreen.kt        # Layar Pelacak Target Khatam Qur'an
│       │   │           ├── QuranReaderScreen.kt    # Layar Mushaf Pembaca Surah & Ayat
│       │   │           ├── SettingsScreen.kt       # Layar Pengaturan (Nama, Adzan, Tema, Metode)
│       │   │           └── ReliabilityCheckScreen.kt # Layar Cek Keandalan Alarm & Panduan OEM
│       │   └── res/
│       │       ├── raw/                        # Audio adzan lokal (Nabawi, Subuh, Ringtone)
│       │       ├── drawable/                   # Aset grafis & ikon launcher
│       │       └── values/                     # Strings & themes XML
│       └── test/java/com/ibadahku/app/domain/
│           ├── PrayerCalculatorTest.kt         # Pengujian unit akurasi waktu sholat
│           ├── QiblaCalculatorTest.kt          # Pengujian unit kalkulasi kiblat 10 kota dunia
│           ├── KhatamPlanTest.kt               # Pengujian unit rekalkulasi target khatam
│           └── TodoTaskTest.kt                 # Pengujian unit logika counter & checklist
├── gradle/
│   ├── libs.versions.toml                      # Version catalog dependensi Gradle
│   └── wrapper/                                # Gradle wrapper executable
├── build.gradle.kts                            # Konfigurasi Gradle root proyek
├── settings.gradle.kts                         # Deklarasi repositori & modul
└── README.md                                   # Dokumentasi lengkap proyek
```

---

## 💻 Panduan Instalasi & Menjalankan

### Prasyarat Sistem

Sebelum memulai, pastikan perangkat komputer Anda memenuhi prasyarat berikut:
- **Operating System:** Windows 10/11 (64-bit), macOS Monterey atau lebih baru, atau Linux (Ubuntu 20.04+).
- **JDK / Java:** Java 21 LTS (direkomendasikan menggunakan Android Studio JBR: `Android Studio\jbr`).
- **Android Studio:** Android Studio Ladybug (2024.2+), Meerkat, atau versi yang lebih baru.
- **Android SDK:**
  - Android SDK Platform 36 / 37
  - Android SDK Build-Tools 36+
  - Android SDK Platform-Tools (ADB)
- **Perangkat Pengujian:**
  - Emulator Android dengan API level 26 hingga 35/36 (Google Play / Google APIs image), ATAU
  - Smartphone Android fisik (Android 8.0 Oreo ke atas) dengan fitur **USB Debugging** aktif.

---

### Langkah 1: Kloning Repositori

Buka terminal (PowerShell, Command Prompt, atau Terminal Linux/macOS):

```bash
git clone https://github.com/username/Ibadahku-app.git
cd Ibadahku-app
```

---

### Langkah 2: Konfigurasi Environment (Java & Android SDK)

Pastikan variabel lingkungan `JAVA_HOME` dan `ANDROID_HOME` telah diarahkan ke instalasi yang benar:

#### Di Windows (PowerShell):
```powershell
# Mengatur JAVA_HOME ke JBR bawaan Android Studio
$env:JAVA_HOME = "C:\Program Files\Android\Android Studio\jbr"
$env:PATH = "$env:JAVA_HOME\bin;$env:PATH"

# Mengatur path ADB
$adb = "$env:LOCALAPPDATA\Android\Sdk\platform-tools\adb.exe"

# Verifikasi Java
java -version
```

#### Di macOS / Linux:
```bash
export JAVA_HOME="/Applications/Android Studio.app/Contents/jbr/Contents/Home"
export PATH="$JAVA_HOME/bin:$PATH"
export ANDROID_HOME="$HOME/Library/Android/sdk"
export PATH="$ANDROID_HOME/platform-tools:$PATH"

java -version
```

---

### Langkah 3: Membuka Proyek di Android Studio

1. Buka **Android Studio**.
2. Pilih menu **File ➔ Open...**, lalu arahkan ke folder `Ibadahku-app`.
3. Tunggu hingga proses **Gradle Sync** selesai secara otomatis.
4. Jika diminta mengunduh SDK Platform atau Build-Tools yang belum terpasang, klik **Install missing platform(s) and sync project**.

---

### Langkah 4: Kompilasi & Build APK (CLI)

Anda dapat melakukan kompilasi file APK debug langsung dari baris perintah terminal tanpa membuka antarmuka berat:

#### Di Windows (PowerShell):
```powershell
$env:JAVA_HOME = "C:\Program Files\Android\Android Studio\jbr"
.\gradlew.bat assembleDebug
```

#### Di macOS / Linux:
```bash
./gradlew assembleDebug
```

Hasil build berkas APK berada di direktori:
```text
app/build/outputs/apk/debug/app-debug.apk
```
*Ukuran APK sangat ramping (~19.9 MB), jauh di bawah batas maksimum efisiensi 40 MB.*

---

### Langkah 5: Menjalankan di Emulator / Perangkat Fisik

#### Cara A: Langsung via Android Studio
1. Pilih konfigurasi run `app` di bagian toolbar atas.
2. Pilih target emulator atau perangkat smartphone fisik Anda yang terhubung.
3. Klik tombol hijau **Run ▶** (atau tekan `Shift + F10`).

#### Cara B: Otomatis via Gradle CLI & ADB
Pastikan emulator sudah berjalan atau smartphone fisik terdeteksi via ADB (`adb devices`):

```powershell
# 1. Install APK langsung ke perangkat aktif
$env:JAVA_HOME = "C:\Program Files\Android\Android Studio\jbr"
.\gradlew.bat installDebug

# 2. Buka aplikasi secara langsung via ADB
& "$env:LOCALAPPDATA\Android\Sdk\platform-tools\adb.exe" shell am start -n com.ibadahku.app/.MainActivity
```

---

## 🧪 Pengujian Unit (Unit Testing)

Seluruh logika matematika astronomis, perbedaan madhab, rumus trigonometri kompas kiblat, target tadarus, dan counter tasbih telah diverifikasi melalui rangkaian unit test otomatis.

Untuk menjalankan seluruh pengujian unit:

#### Di Windows (PowerShell):
```powershell
$env:JAVA_HOME = "C:\Program Files\Android\Android Studio\jbr"
.\gradlew.bat test
```

#### Di macOS / Linux:
```bash
./gradlew test
```

### Cakupan Pengujian:
1. **`PrayerCalculatorTest.kt`**:
   - Validasi jadwal waktu sholat standar Kemenag RI dengan toleransi selisih waktu di bawah ±1 menit.
   - Validasi perbedaan waktu masuk Ashar antara Madhab Syafi'i (bayangan 1x tinggi benda) dan Madhab Hanafi (bayangan 2x tinggi benda).
   - Validasi penyesuaian koreksi menit ihtiyat manual.
2. **`QiblaCalculatorTest.kt`**:
   - Validasi akurasi formula Great-Circle bearing menuju Ka'bah di 10 kota besar dunia (Jakarta, Surabaya, London, Tokyo, New York, Kairo, Istanbul, Sydney, Kuala Lumpur, Makkah). Seluruh hasil memiliki deviasi di bawah 0.1°.
3. **`KhatamPlanTest.kt`**:
   - Validasi pembagian target membaca harian berdasarkan total halaman mushaf (604 halaman) dan durasi target khatam (30, 60, 90, 180, 365 hari).
4. **`TodoTaskTest.kt`**:
   - Validasi preservasi status checklist harian dan logika penambahan serta pengurangan (*undo/reset*) counter tasbih digital.

---

## 🔒 Privasi & Keamanan

- **Zero-Internet Guarantee:** Aplikasi ini tidak menyertakan izin `android.permission.INTERNET`. Tidak ada data pengguna, lokasi GPS, log ibadah, maupun catatan utang sholat yang dikirimkan ke server luar.
- **Tanpa Iklan & Tanpa SDK Pelacak:** Tidak terdapat SDK pihak ketiga (Google AdMob, Facebook Ads, Firebase Analytics, AppsFlyer, dsb.).
- **Penyimpanan Lokal:** Seluruh preferensi dan riwayat tersimpan aman di penyimpanan internal sandboxed perangkat menggunakan Android `SharedPreferences`.

---

## 🤝 Kontribusi & Lisensi

Proyek ini bersifat sumber terbuka (*open source*) di bawah lisensi **MIT License**. Kami menyambut baik kontribusi berupa laporan bug (*issue*), saran fitur baru, maupun pengiriman kode (*pull request*).

```text
Hak Cipta (c) 2026 Tim Pengembang Ibadahku.
Dilisensikan di bawah Lisensi MIT.
```

---
*Dibuat dengan sepenuh hati untuk kemaslahatan dan kekhusyukan ibadah umat Muslim di mana pun berada. بارك الله فيكم.*
