package com.ibadahku.app.data.repository

import android.content.Context
import com.ibadahku.app.data.model.Ayah
import com.ibadahku.app.data.model.KhatamPlan
import com.ibadahku.app.data.model.QuranBookmark
import com.ibadahku.app.data.model.Surah
import com.ibadahku.app.data.model.TadarusProgress
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.LocalDate

class QuranRepository(private val context: Context) {

    private val prefs = context.getSharedPreferences("ibadahku_quran", Context.MODE_PRIVATE)

    private val _progressFlow = MutableStateFlow(loadProgress())
    val progressFlow: StateFlow<TadarusProgress> = _progressFlow.asStateFlow()

    private val _bookmarksFlow = MutableStateFlow(loadBookmarks())
    val bookmarksFlow: StateFlow<List<QuranBookmark>> = _bookmarksFlow.asStateFlow()

    fun loadProgress(): TadarusProgress {
        return TadarusProgress(
            pagesReadToday = prefs.getInt("pages_today", 5),
            targetPagesPerDay = prefs.getInt("target_pages", 10),
            totalPagesRead = prefs.getInt("total_pages", 5),
            totalMushafPages = prefs.getInt("total_mushaf", 30),
            lastSurahName = prefs.getString("last_surah", "Al-Baqarah") ?: "Al-Baqarah",
            lastPage = prefs.getInt("last_page", 5),
            lastAyah = prefs.getInt("last_ayah", 25),
            streakDays = prefs.getInt("streak_days", 7),
            targetDaysTotal = prefs.getInt("target_days", 30)
        )
    }

    fun updateProgress(
        pagesToday: Int,
        lastSurah: String = "Al-Baqarah",
        lastPage: Int = 5,
        lastAyah: Int = 25
    ) {
        val current = _progressFlow.value
        val newProgress = current.copy(
            pagesReadToday = pagesToday,
            totalPagesRead = (current.totalPagesRead + (pagesToday - current.pagesReadToday)).coerceAtLeast(0),
            lastSurahName = lastSurah,
            lastPage = lastPage,
            lastAyah = lastAyah
        )
        prefs.edit().apply {
            putInt("pages_today", newProgress.pagesReadToday)
            putInt("total_pages", newProgress.totalPagesRead)
            putString("last_surah", newProgress.lastSurahName)
            putInt("last_page", newProgress.lastPage)
            putInt("last_ayah", newProgress.lastAyah)
            apply()
        }
        _progressFlow.value = newProgress
    }

    fun setTargetPages(target: Int) {
        prefs.edit().putInt("target_pages", target).apply()
        _progressFlow.value = _progressFlow.value.copy(targetPagesPerDay = target)
    }

    private fun loadBookmarks(): List<QuranBookmark> {
        return listOf(
            QuranBookmark("1", 2, "Al-Baqarah", 25, 5, "Halaman terakhir tadarus subuh"),
            QuranBookmark("2", 18, "Al-Kahfi", 1, 293, "Persiapan sunnah hari Jumat"),
            QuranBookmark("3", 67, "Al-Mulk", 1, 562, "Rutinitas sebelum tidur")
        )
    }

    fun addBookmark(surahNumber: Int, surahName: String, ayahNumber: Int, pageNumber: Int, note: String) {
        val current = _bookmarksFlow.value.toMutableList()
        current.add(0, QuranBookmark(
            id = System.currentTimeMillis().toString(),
            surahNumber = surahNumber,
            surahName = surahName,
            ayahNumber = ayahNumber,
            pageNumber = pageNumber,
            note = note
        ))
        _bookmarksFlow.value = current
    }

    fun getKhatamPlan(targetDays: Int = 30): KhatamPlan {
        val totalPages = 604
        val daily = totalPages.toDouble() / targetDays.toDouble()
        val finishDate = LocalDate.now().plusDays(targetDays.toLong())
        return KhatamPlan(
            id = "plan_$targetDays",
            targetDays = targetDays,
            startDateTimestamp = System.currentTimeMillis(),
            dailyPages = Math.round(daily * 10.0) / 10.0,
            remainingPages = totalPages - _progressFlow.value.totalPagesRead,
            estimatedFinishDate = finishDate.toString()
        )
    }

    val surahList: List<Surah> = listOf(
        Surah(1, "الفاتحة", "Al-Fatihah", "Pembukaan", 7, 1, "Makkiyyah"),
        Surah(2, "البقرة", "Al-Baqarah", "Sapi Betina", 286, 2, "Madaniyyah"),
        Surah(3, "آل عمران", "Ali 'Imran", "Keluarga Imran", 200, 50, "Madaniyyah"),
        Surah(4, "النساء", "An-Nisa'", "Wanita", 176, 77, "Madaniyyah"),
        Surah(5, "المائدة", "Al-Ma'idah", "Hidangan", 120, 106, "Madaniyyah"),
        Surah(6, "الأنعام", "Al-An'am", "Binatang Ternak", 165, 128, "Makkiyyah"),
        Surah(7, "الأعراف", "Al-A'raf", "Tempat Tertinggi", 206, 151, "Makkiyyah"),
        Surah(8, "الأنفال", "Al-Anfal", "Rampasan Perang", 75, 177, "Madaniyyah"),
        Surah(9, "التوبة", "At-Taubah", "Pengampunan", 129, 187, "Madaniyyah"),
        Surah(10, "يونس", "Yunus", "Nabi Yunus", 109, 208, "Makkiyyah"),
        Surah(18, "الكهف", "Al-Kahfi", "Penghuni Gua", 110, 293, "Makkiyyah"),
        Surah(36, "يس", "Yasin", "Yasin", 83, 440, "Makkiyyah"),
        Surah(55, "الرحمن", "Ar-Rahman", "Maha Pengasih", 78, 531, "Madaniyyah"),
        Surah(56, "الواقعة", "Al-Waqi'ah", "Hari Kiamat", 96, 534, "Makkiyyah"),
        Surah(67, "الملك", "Al-Mulk", "Kerajaan", 30, 562, "Makkiyyah"),
        Surah(112, "الإخلاص", "Al-Ikhlas", "Keesaan Allah", 4, 604, "Makkiyyah"),
        Surah(113, "الفلق", "Al-Falaq", "Waktu Subuh", 5, 604, "Makkiyyah"),
        Surah(114, "الناس", "An-Nas", "Manusia", 6, 604, "Makkiyyah")
    )

    fun getVersesForSurah(surahNumber: Int): List<Ayah> {
        return when (surahNumber) {
            1 -> listOf(
                Ayah(1, 1, "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ", "Bismillahir-rahmanir-rahim", "Dengan nama Allah Yang Maha Pengasih, Maha Penyayang.", 1, 1),
                Ayah(2, 2, "الْحَمْدُ لِلَّهِ رَبِّ الْعَالَمِينَ", "Al-hamdu lillahi rabbil-'alamin", "Segala puji bagi Allah, Tuhan seluruh alam.", 1, 1),
                Ayah(3, 3, "الرَّحْمَٰنِ الرَّحِيمِ", "Ar-rahmanir-rahim", "Yang Maha Pengasih, Maha Penyayang.", 1, 1),
                Ayah(4, 4, "مَالِكِ يَوْمِ الدِّينِ", "Maliki yaumid-din", "Pemilik hari pembalasan.", 1, 1),
                Ayah(5, 5, "إِيَّاكَ نَعْبُدُ وَإِيَّاكَ نَسْتَعِينُ", "Iyyaka na'budu wa iyyaka nasta'in", "Hanya kepada Engkaulah kami menyembah dan hanya kepada Engkaulah kami memohon pertolongan.", 1, 1),
                Ayah(6, 6, "اهْدِنَا الصِّرَاطَ الْمُسْتَقِيمَ", "Ihdinas-siratal-mustaqim", "Tunjukilah kami jalan yang lurus,", 1, 1),
                Ayah(7, 7, "صِرَاطَ الَّذِينَ أَنْعَمْتَ عَلَيْهِمْ غَيْرِ الْمَغْضُوبِ عَلَيْهِمْ وَلَا الضَّالِّينَ", "Siratalladhina an'amta 'alaihim ghairil-maghdubi 'alaihim walad-dallin", "(yaitu) jalan orang-orang yang telah Engkau beri nikmat kepadanya; bukan (jalan) mereka yang dimurkai dan bukan (pula jalan) mereka yang sesat.", 1, 1)
            )
            2 -> listOf(
                Ayah(8, 1, "الم", "Alif-Lam-Mim", "Alif Lam Mim.", 2, 1),
                Ayah(9, 2, "ذَٰلِكَ الْكِتَابُ لَا رَيْبَ ۛ فِيهِ ۛ هُدًى لِّلْمُتَّقِينَ", "Zalikal-kitabu la raiba fih, hudal-lil-muttaqin", "Kitab (Al-Qur'an) ini tidak ada keraguan padanya; petunjuk bagi mereka yang bertakwa,", 2, 1),
                Ayah(10, 3, "الَّذِينَ يُؤْمِنُونَ بِالْغَيْبِ وَيُقِيمُونَ الصَّلَاةَ وَمِمَّا رَزَقْنَاهُمْ يُنفِقُونَ", "Alladhina yu'minuna bil-ghaibi wa yuqimunas-salata wa mimma razaqnahum yunfiqun", "(yaitu) mereka yang beriman kepada yang gaib, melaksanakan salat, dan menginfakkan sebagian rezeki yang Kami berikan kepada mereka,", 2, 1),
                Ayah(11, 4, "وَالَّذِينَ يُؤْمِنُونَ بِمَا أُنزِلَ إِلَيْكَ وَمَا أُنزِلَ مِن قَبْلِكَ وَبِالْآخِرَةِ هُمْ يُوقِنُونَ", "Walladhina yu'minuna bima unzila ilaika wa ma unzila min qablika wa bil-akhirati hum yuqinun", "dan mereka yang beriman kepada (Al-Qur'an) yang diturunkan kepadamu dan (kitab-kitab) yang telah diturunkan sebelum engkau, serta mereka yakin akan adanya akhirat.", 2, 1),
                Ayah(12, 5, "أُولَٰئِكَ عَلَىٰ هُدًى مِّن رَّبِّهِمْ ۖ وَأُولَٰئِكَ هُمُ الْمُفْلِحُونَ", "Ula'ika 'ala hudam-mir-rabbihim wa ula'ika humul-muflihun", "Merekalah yang mendapat petunjuk dari Tuhannya, dan mereka itulah orang-orang yang beruntung.", 2, 1)
            )
            67 -> listOf(
                Ayah(5260, 1, "تَبَارَكَ الَّذِي بِيَدِهِ الْمُلْكُ وَهُوَ عَلَىٰ كُلِّ شَيْءٍ قَدِيرٌ", "Tabarakalladhi biyadihil-mulku wa huwa 'ala kulli syai'in qadir", "Mahasuci Allah yang di tangan-Nyalah segala kerajaan, dan Dia Mahakuasa atas segala sesuatu.", 562, 29),
                Ayah(5261, 2, "الَّذِي خَلَقَ الْمَوْتَ وَالْحَيَاةَ لِيَبْلُوَكُمْ أَيُّكُمْ أَحْسَنُ عَمَلًا ۚ وَهُوَ الْعَزِيزُ الْغَفُورُ", "Alladhi khalaqal-mauta wal-hayata liyabluwakum ayyukum ahsanu 'amala, wa huwal-'azizul-ghafur", "Yang menciptakan mati dan hidup, untuk menguji kamu, siapa di antara kamu yang lebih baik amalnya. Dan Dia Mahaperkasa, Maha Pengampun.", 562, 29),
                Ayah(5262, 3, "الَّذِي خَلَقَ سَبْعَ سَمَاوَاتٍ طِبَاقًا ۖ مَّا تَرَىٰ فِي خَلْقِ الرَّحْمَٰنِ مِن تَفَاوُتٍ", "Alladhi khalaqa sab'a samawatin tibaqa, ma tara fi khalqir-rahmani min tafawut", "Yang menciptakan tujuh langit berlapis-lapis. Tidak akan kamu lihat sesuatu yang tidak seimbang pada ciptaan Tuhan Yang Maha Pengasih.", 562, 29)
            )
            112 -> listOf(
                Ayah(6222, 1, "قُلْ هُوَ اللَّهُ أَحَدٌ", "Qul huwallahu ahad", "Katakanlah (Muhammad), 'Dialah Allah, Yang Maha Esa.'", 604, 30),
                Ayah(6223, 2, "اللَّهُ الصَّمَدُ", "Allahus-samad", "Allah tempat meminta segala sesuatu.", 604, 30),
                Ayah(6224, 3, "لَمْ يَلِدْ وَلَمْ يُولَدْ", "Lam yalid wa lam yulad", "(Allah) tidak beranak dan tidak pula diperanakkan,", 604, 30),
                Ayah(6225, 4, "وَلَمْ يَكُن لَّهُ كُفُوًا أَحَدٌ", "Wa lam yakul-lahu kufuwan ahad", "dan tidak ada sesuatu yang setara dengan Dia.", 604, 30)
            )
            else -> listOf(
                Ayah(1, 1, "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ", "Bismillahir-rahmanir-rahim", "Dengan nama Allah Yang Maha Pengasih, Maha Penyayang.", 1, 1)
            )
        }
    }
}
