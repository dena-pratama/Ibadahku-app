package com.ibadahku.app

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.ibadahku.app.data.model.PrayerStatus
import com.ibadahku.app.data.model.PrayerType
import com.ibadahku.app.data.model.ReminderMode
import com.ibadahku.app.data.model.TaskType
import com.ibadahku.app.data.repository.AppPreferencesRepository
import com.ibadahku.app.data.repository.QuranRepository
import com.ibadahku.app.data.repository.TodoRepository
import com.ibadahku.app.domain.PrayerCalculator
import com.ibadahku.app.engine.CompassSensorManager
import com.ibadahku.app.engine.PrayerAlarmScheduler
import com.ibadahku.app.ui.components.AddTaskDialog
import com.ibadahku.app.ui.components.CompletionDialog
import com.ibadahku.app.ui.navigation.Screen
import com.ibadahku.app.ui.screens.HomeScreen
import com.ibadahku.app.ui.screens.PrayerScheduleScreen
import com.ibadahku.app.ui.screens.QadhaTrackerScreen
import com.ibadahku.app.ui.screens.QiblaScreen
import com.ibadahku.app.ui.screens.QuranReaderScreen
import com.ibadahku.app.ui.screens.ReliabilityCheckScreen
import com.ibadahku.app.ui.screens.SettingsScreen
import com.ibadahku.app.ui.screens.SplashScreen
import com.ibadahku.app.ui.screens.TadarusScreen
import com.ibadahku.app.ui.screens.TasbihScreen
import com.ibadahku.app.ui.screens.TodoScreen
import com.ibadahku.app.ui.theme.CreamBackground
import com.ibadahku.app.ui.theme.ForestGreen
import com.ibadahku.app.ui.theme.IbadahkuTheme
import com.ibadahku.app.ui.theme.SageContainer
import com.ibadahku.app.ui.theme.TextDark
import com.ibadahku.app.ui.theme.TextSecondary
import java.time.LocalDate

class MainActivity : ComponentActivity() {

    private lateinit var prefsRepo: AppPreferencesRepository
    private lateinit var quranRepo: QuranRepository
    private lateinit var todoRepo: TodoRepository
    private lateinit var compassSensorManager: CompassSensorManager
    private lateinit var alarmScheduler: PrayerAlarmScheduler

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        // Permission result handled
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        prefsRepo = AppPreferencesRepository(this)
        quranRepo = QuranRepository(this)
        todoRepo = TodoRepository(this)
        compassSensorManager = CompassSensorManager(this)
        alarmScheduler = PrayerAlarmScheduler(this)

        // Request notification permission if Android 13+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }

        setContent {
            val appTheme by prefsRepo.themeFlow.collectAsState()
            val systemDark = androidx.compose.foundation.isSystemInDarkTheme()
            val isDarkTheme = when (appTheme) {
                "dark" -> true
                "light" -> false
                else -> systemDark
            }

            IbadahkuTheme(darkTheme = isDarkTheme) {
                MainContent(isDarkTheme = isDarkTheme, appTheme = appTheme)
            }
        }
    }

    @Composable
    private fun MainContent(isDarkTheme: Boolean, appTheme: String) {
        val navController = rememberNavController()
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        val settings by prefsRepo.settingsFlow.collectAsState()
        val tasks by todoRepo.tasksFlow.collectAsState()
        val quranProgress by quranRepo.progressFlow.collectAsState()

        var showCompletionDialog by remember { mutableStateOf(false) }
        var showAddTaskDialog by remember { mutableStateOf(false) }

        // Update compass target coordinates
        remember(settings.latitude, settings.longitude) {
            compassSensorManager.updateCoordinates(settings.latitude, settings.longitude)
            true
        }

        // Today's prayer statuses
        val today = LocalDate.now()
        val prayerStatuses = remember(today, settings) {
            PrayerType.values().associateWith { type ->
                prefsRepo.getPrayerStatus(today, type)
            }
        }
        val reminderModes = remember(settings) {
            PrayerType.values().associateWith { type ->
                prefsRepo.getReminderMode(type)
            }
        }

        val schedule = remember(settings, today, prayerStatuses, reminderModes) {
            PrayerCalculator.calculateSchedule(
                date = today,
                settings = settings,
                prayerStatuses = prayerStatuses,
                reminderModes = reminderModes
            )
        }

        val showBottomBar = currentRoute in listOf(
            Screen.Home.route,
            Screen.PrayerSchedule.route,
            Screen.Settings.route
        )

        Scaffold(
            bottomBar = {
                if (showBottomBar) {
                    NavigationBar(
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(elevation = 12.dp, shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                            .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)),
                        containerColor = MaterialTheme.colorScheme.surface,
                        tonalElevation = 8.dp
                    ) {
                        NavigationBarItem(
                            selected = (currentRoute == Screen.Home.route),
                            onClick = {
                                navController.navigate(Screen.Home.route) {
                                    popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = {
                                Icon(
                                    imageVector = if (currentRoute == Screen.Home.route) Icons.Filled.Home else Icons.Outlined.Home,
                                    contentDescription = "Beranda",
                                    modifier = Modifier.size(24.dp)
                                )
                            },
                            label = { Text("Beranda", fontSize = 11.sp, fontWeight = FontWeight.Medium) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = MaterialTheme.colorScheme.primary,
                                selectedTextColor = MaterialTheme.colorScheme.primary,
                                unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                indicatorColor = MaterialTheme.colorScheme.surfaceVariant
                            )
                        )

                        NavigationBarItem(
                            selected = (currentRoute == Screen.PrayerSchedule.route),
                            onClick = {
                                navController.navigate(Screen.PrayerSchedule.route) {
                                    popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = {
                                Icon(
                                    imageVector = if (currentRoute == Screen.PrayerSchedule.route) Icons.Filled.BarChart else Icons.Outlined.BarChart,
                                    contentDescription = "Riwayat",
                                    modifier = Modifier.size(24.dp)
                                )
                            },
                            label = { Text("Riwayat", fontSize = 11.sp, fontWeight = FontWeight.Medium) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = MaterialTheme.colorScheme.primary,
                                selectedTextColor = MaterialTheme.colorScheme.primary,
                                unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                indicatorColor = MaterialTheme.colorScheme.surfaceVariant
                            )
                        )

                        NavigationBarItem(
                            selected = (currentRoute == Screen.Settings.route),
                            onClick = {
                                navController.navigate(Screen.Settings.route) {
                                    popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = {
                                Icon(
                                    imageVector = Icons.Outlined.Settings,
                                    contentDescription = "Pengaturan",
                                    modifier = Modifier.size(24.dp)
                                )
                            },
                            label = { Text("Pengaturan", fontSize = 11.sp, fontWeight = FontWeight.Medium) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = MaterialTheme.colorScheme.primary,
                                selectedTextColor = MaterialTheme.colorScheme.primary,
                                unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                indicatorColor = MaterialTheme.colorScheme.surfaceVariant
                            )
                        )
                    }
                }
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                NavHost(
                    navController = navController,
                    startDestination = Screen.Splash.route
                ) {
                    // 1. Splash Screen
                    composable(Screen.Splash.route) {
                        SplashScreen(
                            onSplashFinished = {
                                navController.navigate(Screen.Home.route) {
                                    popUpTo(Screen.Splash.route) { inclusive = true }
                                }
                            }
                        )
                    }

                    // 2. Beranda (Home)
                    composable(Screen.Home.route) {
                        HomeScreen(
                            prayerSchedule = schedule,
                            tasks = tasks,
                            userName = settings.userName,
                            onNavigateToSchedule = { navController.navigate(Screen.PrayerSchedule.route) },
                            onNavigateToTadarus = { navController.navigate(Screen.Tadarus.route) },
                            onNavigateToTodo = { navController.navigate(Screen.Todo.route) },
                            onNavigateToQibla = { navController.navigate(Screen.Qibla.route) },
                            onNavigateToSettings = { navController.navigate(Screen.Settings.route) },
                            onNavigateToQadha = { navController.navigate(Screen.QadhaTracker.route) }
                        )
                    }

                    // 3. Jadwal Sholat & Riwayat
                    composable(Screen.PrayerSchedule.route) {
                        PrayerScheduleScreen(
                            schedule = schedule,
                            onBackClick = { navController.popBackStack() },
                            onToggleReminder = { prayerType ->
                                val currentMode = prefsRepo.getReminderMode(prayerType)
                                val nextMode = if (currentMode == ReminderMode.DISABLED) {
                                    ReminderMode.ADZAN_FULL
                                } else {
                                    ReminderMode.DISABLED
                                }
                                prefsRepo.setReminderMode(prayerType, nextMode)
                                alarmScheduler.scheduleAlarms(settings)
                            },
                            onTogglePrayerStatus = { prayerType ->
                                val currentStatus = prefsRepo.getPrayerStatus(today, prayerType)
                                val newStatus = if (currentStatus == PrayerStatus.DONE) PrayerStatus.PENDING else PrayerStatus.DONE
                                prefsRepo.setPrayerStatus(today, prayerType, newStatus)
                                if (newStatus == PrayerStatus.DONE) {
                                    showCompletionDialog = true
                                }
                            },
                            onNavigateToQadha = { navController.navigate(Screen.QadhaTracker.route) },
                            onNavigateToTadarus = { navController.navigate(Screen.Tadarus.route) },
                            qadhaTotal = prefsRepo.getTotalQadha(),
                            completedTasksCount = tasks.count { it.isCompleted },
                            quranPagesRead = quranProgress.pagesReadToday
                        )
                    }

                    // 4. Tadarus Al-Qur'an
                    composable(Screen.Tadarus.route) {
                        TadarusScreen(
                            progress = quranProgress,
                            onBackClick = { navController.popBackStack() },
                            onContinueReading = {
                                navController.navigate(Screen.QuranReader.createRoute(2))
                            },
                            onOpenSurahList = {
                                navController.navigate(Screen.QuranReader.createRoute(1))
                            }
                        )
                    }

                    // Quran Reader
                    composable(
                        route = Screen.QuranReader.route,
                        arguments = listOf(navArgument("surahNumber") { type = NavType.IntType })
                    ) { backStack ->
                        val surahNum = backStack.arguments?.getInt("surahNumber") ?: 2
                        QuranReaderScreen(
                            quranRepo = quranRepo,
                            initialSurahNumber = surahNum,
                            onBackClick = { navController.popBackStack() },
                            onPageCompleted = {
                                quranRepo.updateProgress(
                                    pagesToday = quranProgress.pagesReadToday + 1,
                                    lastSurah = "Al-Baqarah",
                                    lastPage = quranProgress.lastPage + 1
                                )
                                showCompletionDialog = true
                            }
                        )
                    }

                    // 5. To-Do Ibadah
                    composable(Screen.Todo.route) {
                        TodoScreen(
                            tasks = tasks,
                            onBackClick = { navController.popBackStack() },
                            onToggleTask = { taskId ->
                                val completed = todoRepo.toggleTask(taskId)
                                if (completed) {
                                    showCompletionDialog = true
                                }
                            },
                            onOpenCounter = { task ->
                                navController.navigate(Screen.Tasbih.createRoute(task.id))
                            },
                            onAddTaskClick = { showAddTaskDialog = true },
                            onNavigateToQadha = { navController.navigate(Screen.QadhaTracker.route) }
                        )
                    }

                    // Digital Tasbih Screen
                    composable(
                        route = Screen.Tasbih.route,
                        arguments = listOf(navArgument("taskId") { type = NavType.StringType })
                    ) { backStack ->
                        val taskId = backStack.arguments?.getString("taskId") ?: "2"
                        val task = tasks.find { it.id == taskId } ?: tasks.first()
                        TasbihScreen(
                            task = task,
                            onBackClick = { navController.popBackStack() },
                            onIncrement = {
                                val completed = todoRepo.incrementCounter(taskId, 1)
                                if (completed) {
                                    showCompletionDialog = true
                                }
                            },
                            onUndo = { todoRepo.undoCounter(taskId) },
                            onReset = { todoRepo.resetCounter(taskId) }
                        )
                    }

                    // 6. Arah Kiblat
                    composable(Screen.Qibla.route) {
                        QiblaScreen(
                            compassSensorManager = compassSensorManager,
                            onBackClick = { navController.popBackStack() }
                        )
                    }

                    // 7. Pengaturan
                    composable(Screen.Settings.route) {
                        SettingsScreen(
                            settings = settings,
                            appTheme = appTheme,
                            onSelectTheme = { prefsRepo.setAppTheme(it) },
                            onBackClick = { navController.popBackStack() },
                            onUpdateSettings = { newSettings ->
                                prefsRepo.updateSettings(newSettings)
                                alarmScheduler.scheduleAlarms(newSettings)
                            },
                            onNavigateToReliabilityCheck = {
                                navController.navigate(Screen.ReliabilityCheck.route)
                            }
                        )
                    }

                    // Reliability Check (Cek Keandalan)
                    composable(Screen.ReliabilityCheck.route) {
                        ReliabilityCheckScreen(
                            onBackClick = { navController.popBackStack() }
                        )
                    }

                    // Qadha Sholat Tracker
                    composable(Screen.QadhaTracker.route) {
                        QadhaTrackerScreen(
                            prefsRepo = prefsRepo,
                            onBackClick = { navController.popBackStack() }
                        )
                    }
                }

                // Completion Celebration Dialog Modal (Screen 8)
                CompletionDialog(
                    visible = showCompletionDialog,
                    onDismiss = { showCompletionDialog = false }
                )

                // Add Task Dialog
                AddTaskDialog(
                    visible = showAddTaskDialog,
                    onDismiss = { showAddTaskDialog = false },
                    onTaskCreated = { newTask ->
                        todoRepo.addTask(newTask)
                    }
                )
            }
        }
    }
}
