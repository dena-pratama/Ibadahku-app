package com.ibadahku.app.data.model

data class DzikirItem(
    val id: String,
    val title: String,
    val category: TaskCategory,
    val arabicText: String,
    val latinText: String,
    val translation: String,
    val fadhilah: String,
    val targetCount: Int = 100
)

object DzikirLibrary {
    val items: List<DzikirItem> = listOf(
        DzikirItem(
            id = "sayyidul_istighfar",
            title = "Sayyidul Istighfar",
            category = TaskCategory.DZIKIR,
            arabicText = "اللَّهُمَّ أَنْتَ رَبِّي لَا إِلَهَ إِلَّا أَنْتَ خَلَقْتَنِي وَأَنَا عَبْدُكَ وَأَنَا عَلَى عَهْدِكَ وَوَعْدِكَ مَا اسْتَطَعْتُ أَعُوذُ بِكَ مِنْ شَرِّ مَا صَنَعْتُ أَبُوءُ لَكَ بِنِعْمَتِكَ عَلَيَّ وَأَبُوءُ بِذَنْبِي فَاغْفِرْ لِي فَإِنَّهُ لَا يَغْفِرُ الذُّنُوبَ إِلَّا أَنْتَ",
            latinText = "Allahumma anta rabbi la ilaha illa anta, khalaqtani wa ana 'abduka, wa ana 'ala 'ahdika wa wa'dika mastatha'tu, a'udzu bika min syarri ma shana'tu, abu-u laka bini'matika 'alayya, wa abu-u bidzanbi faghfirli fa-innahu la yaghfirudz-dzunuba illa anta.",
            translation = "Ya Allah, Engkau adalah Tuhanku, tidak ada Tuhan selain Engkau. Engkau yang menciptakan aku dan aku adalah hamba-Mu. Aku memohon ampunan-Mu atas segala dosaku.",
            fadhilah = "Barangsiapa membacanya di petang hari lalu meninggal di malamnya, ia masuk surga. (HR. Bukhari)",
            targetCount = 3
        ),
        DzikirItem(
            id = "sholawat_ibrahimiyah",
            title = "Sholawat Ibrahimiyah",
            category = TaskCategory.SHOLAWAT,
            arabicText = "اللَّهُمَّ صَلِّ عَلَى مُحَمَّدٍ وَعَلَى آلِ مُحَمَّدٍ كَمَا صَلَّيْتَ عَلَى إِبْرَاهِيمَ وَعَلَى آلِ إِبْرَاهِيمَ إِنَّكَ حَمِيدٌ مَجِيدٌ",
            latinText = "Allahumma shalli 'ala Muhammad wa 'ala ali Muhammad, kama shallaita 'ala Ibrahim wa 'ala ali Ibrahim, innaka hamidum-majid.",
            translation = "Ya Allah, limpahkanlah rahmat kepada Nabi Muhammad dan keluarga Nabi Muhammad, sebagaimana Engkau telah melimpahkan rahmat kepada Nabi Ibrahim dan keluarga Nabi Ibrahim.",
            fadhilah = "Sholawat paling afdhal yang diajarkan langsung oleh Rasulullah shallallahu 'alaihi wasallam.",
            targetCount = 100
        ),
        DzikirItem(
            id = "sholawat_nariyah",
            title = "Sholawat Nariyah (Tafrijiyyah)",
            category = TaskCategory.SHOLAWAT,
            arabicText = "اللَّهُمَّ صَلِّ صَلَاةً كَامِلَةً وَسَلِّمْ سَلَامًا تَامًّا عَلَى سَيِّدِنَا مُحَمَّدٍ الَّذِي تَنْحَلُّ بِهِ الْعُقَدُ وَتَنْفَرِجُ بِهِ الْكُرَبُ",
            latinText = "Allahumma shalli shalatan kamilatan wa sallim salaman tamman 'ala sayyidina Muhammadinilladzi tanhallu bihil-'uqad wa tanfariju bihil-kurab.",
            translation = "Ya Allah, limpahkanlah shalawat yang sempurna dan keselamatan yang menyeluruh kepada junjungan kami Nabi Muhammad yang dengannya terurai segala ikatan dan terbuka jalan keluar dari kesusahan.",
            fadhilah = "Membuka pintu kelapangan, kemudahan hajat, dan ketenteraman hati.",
            targetCount = 100
        ),
        DzikirItem(
            id = "sholawat_munjiyat",
            title = "Sholawat Munjiyat",
            category = TaskCategory.SHOLAWAT,
            arabicText = "اللَّهُمَّ صَلِّ عَلَى سَيِّدِنَا مُحَمَّدٍ صَلَاةً تُنْجِينَا بِهَا مِنْ جَمِيعِ الْأَهْوَالِ وَالْآفَاتِ",
            latinText = "Allahumma shalli 'ala sayyidina Muhammadin shalatan tunjina biha min jami'il-ahwali wal-afat.",
            translation = "Ya Allah, limpahkanlah rahmat kepada junjungan kami Nabi Muhammad, rahmat yang dapat menyelamatkan kami dari segala bencana dan malapetaka.",
            fadhilah = "Penyelamat dari marabahaya dan pemenuhan segala kebaikan dunia akhirat.",
            targetCount = 100
        ),
        DzikirItem(
            id = "tasbih_tahmid_takbir",
            title = "Tasbih, Tahmid, Takbir",
            category = TaskCategory.DZIKIR,
            arabicText = "سُبْحَانَ اللَّهِ ، وَالْحَمْدُ لِلَّهِ ، وَلَا إِلَهَ إِلَّا اللَّهُ ، وَاللَّهُ أَكْبَرُ",
            latinText = "Subhanallah, walhamdulillah, wa la ilaha illallah, wallahu akbar.",
            translation = "Mahasuci Allah, segala puji bagi Allah, tiada Tuhan selain Allah, dan Allah Mahabesar.",
            fadhilah = "Kalimat yang paling dicintai Allah dan lebih baik dari apa yang disinari matahari. (HR. Muslim)",
            targetCount = 100
        ),
        DzikirItem(
            id = "hauqalah",
            title = "Hauqalah",
            category = TaskCategory.DZIKIR,
            arabicText = "لَا حَوْلَ وَلَا قُوَّةَ إِلَّا بِاللَّهِ الْعَلِيِّ الْعَظِيمِ",
            latinText = "La hawla wa la quwwata illa billahil-'aliyyil-'azhim.",
            translation = "Tiada daya dan upaya kecuali dengan pertolongan Allah Yang Mahatinggi lagi Mahaagung.",
            fadhilah = "Simpanan perbendaharaan surga dan penawar dari 99 penyakit. (HR. Bukhari)",
            targetCount = 100
        ),
        DzikirItem(
            id = "dzikir_perlindungan",
            title = "Dzikir Perlindungan Pagi-Petang",
            category = TaskCategory.DZIKIR,
            arabicText = "بِسْمِ اللَّهِ الَّذِي لَا يَضُرُّ مَعَ اسْمِهِ شَيْءٌ فِي الْأَرْضِ وَلَا فِي السَّمَاءِ وَهُوَ السَّمِيعُ الْعَلِيمُ",
            latinText = "Bismillahilladzi la yadhurru ma'asmihi syai'un fil-ardhi wa la fis-sama'i wa huwas-sami'ul-'alim.",
            translation = "Dengan nama Allah yang bersama nama-Nya tidak ada sesuatu pun di bumi dan di langit yang dapat mendatangkan bahaya, dan Dia Maha Mendengar lagi Maha Mengetahui.",
            fadhilah = "Dibaca 3x di waktu pagi dan petang, tidak akan ada bahaya yang mencelakainya. (HR. Abu Dawud & Tirmidzi)",
            targetCount = 33
        ),
        DzikirItem(
            id = "istighfar_harian",
            title = "Istighfar Harian",
            category = TaskCategory.DZIKIR,
            arabicText = "أَسْتَغْفِرُ اللَّهَ الْعَظِيمَ وَأَتُوبُ إِلَيْهِ",
            latinText = "Astaghfirullahal-'azhim wa atubu ilaih.",
            translation = "Aku memohon ampun kepada Allah Yang Mahaagung dan aku bertaubat kepada-Nya.",
            fadhilah = "Rasulullah shallallahu 'alaihi wasallam beristighfar lebih dari 70–100 kali sehari.",
            targetCount = 100
        ),
        DzikirItem(
            id = "tasbih_fatimah",
            title = "Tasbih Sayyidah Fatimah",
            category = TaskCategory.DZIKIR,
            arabicText = "سُبْحَانَ اللَّهِ (٣٣×) ، الْحَمْدُ لِلَّهِ (٣٣×) ، اللَّهُ أَكْبَرُ (٣٤×)",
            latinText = "Subhanallah (33x), Alhamdulillah (33x), Allahu Akbar (34x)",
            translation = "Mahasuci Allah, Segala puji bagi Allah, Allah Mahabesar.",
            fadhilah = "Dibaca sebelum tidur atau setelah sholat fardhu, lebih baik daripada seorang pelayan pembantu.",
            targetCount = 100
        ),
        DzikirItem(
            id = "sholawat_pendek",
            title = "Sholawat Nabi Ringkas",
            category = TaskCategory.SHOLAWAT,
            arabicText = "اللَّهُمَّ صَلِّ عَلَى سَيِّدِنَا مُحَمَّدٍ وَعَلَى آلِهِ وَسَلِّمْ",
            latinText = "Allahumma shalli 'ala sayyidina Muhammadin wa 'ala alihi wa sallim.",
            translation = "Ya Allah, limpahkanlah rahmat dan keselamatan kepada junjungan kami Nabi Muhammad beserta keluarganya.",
            fadhilah = "Barangsiapa bershalawat kepadaku sekali, Allah akan bershalawat kepadanya sepuluh kali. (HR. Muslim)",
            targetCount = 100
        )
    )
}
