package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.ui.theme.*
import com.example.util.AppLanguage
import com.example.util.Strings

/**
 * Modern Compact Language Switcher Chip (suitable for Top Bars, Navigation Bars, and Headers).
 * Displays the current locale flag with short label and opens modal or toggles directly.
 */
@Composable
fun LanguageSwitcherChip(
    currentLanguage: AppLanguage,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .clickable(onClick = onClick)
            .border(1.dp, DevCyan.copy(alpha = 0.45f), RoundedCornerShape(20.dp)),
        color = DevCardGlass
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 11.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Text(
                text = currentLanguage.flagEmoji,
                fontSize = 13.sp
            )
            Text(
                text = if (currentLanguage == AppLanguage.BANGLA) "বাংলা" else "EN",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = DevCyan
            )
            Icon(
                imageVector = Icons.Default.SwapHoriz,
                contentDescription = "Switch Language",
                tint = DevCyan.copy(alpha = 0.8f),
                modifier = Modifier.size(13.dp)
            )
        }
    }
}

/**
 * Modern Segmented Language Switcher (Bengali / English).
 * Suitable for Drawer, Settings screens, or Profile sections.
 */
@Composable
fun LanguageSegmentedControl(
    selectedLanguage: AppLanguage,
    onLanguageSelected: (AppLanguage) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .border(1.dp, DevBorderGlass, RoundedCornerShape(16.dp)),
        color = DevCardGlass
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            // Bangla Option
            val isBn = selectedLanguage == AppLanguage.BANGLA
            val bnBgColor by animateColorAsState(
                targetValue = if (isBn) DevCyan.copy(alpha = 0.18f) else Color.Transparent,
                animationSpec = tween(250, easing = FastOutSlowInEasing),
                label = "bnBg"
            )
            val bnBorderColor by animateColorAsState(
                targetValue = if (isBn) DevCyan else Color.Transparent,
                animationSpec = tween(250, easing = FastOutSlowInEasing),
                label = "bnBorder"
            )

            Surface(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(12.dp))
                    .clickable { onLanguageSelected(AppLanguage.BANGLA) }
                    .border(1.dp, bnBorderColor, RoundedCornerShape(12.dp)),
                color = bnBgColor
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp, horizontal = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(text = "🇧🇩 ", fontSize = 15.sp)
                    Text(
                        text = "বাংলা",
                        fontSize = 13.sp,
                        fontWeight = if (isBn) FontWeight.Bold else FontWeight.Medium,
                        color = if (isBn) DevCyan else DevTextSecondary
                    )
                    if (isBn) {
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = DevCyan,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }
            }

            // English Option
            val isEn = selectedLanguage == AppLanguage.ENGLISH
            val enBgColor by animateColorAsState(
                targetValue = if (isEn) DevPurple.copy(alpha = 0.18f) else Color.Transparent,
                animationSpec = tween(250, easing = FastOutSlowInEasing),
                label = "enBg"
            )
            val enBorderColor by animateColorAsState(
                targetValue = if (isEn) DevPurple else Color.Transparent,
                animationSpec = tween(250, easing = FastOutSlowInEasing),
                label = "enBorder"
            )

            Surface(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(12.dp))
                    .clickable { onLanguageSelected(AppLanguage.ENGLISH) }
                    .border(1.dp, enBorderColor, RoundedCornerShape(12.dp)),
                color = enBgColor
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp, horizontal = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(text = "🌐 ", fontSize = 15.sp)
                    Text(
                        text = "English",
                        fontSize = 13.sp,
                        fontWeight = if (isEn) FontWeight.Bold else FontWeight.Medium,
                        color = if (isEn) DevPurple else DevTextSecondary
                    )
                    if (isEn) {
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = DevPurple,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }
            }
        }
    }
}

/**
 * Full Language Selection Dialog Component with persistence callback.
 */
@Composable
fun LanguageSelectionDialog(
    currentLanguage: AppLanguage,
    onLanguageSelected: (AppLanguage) -> Unit,
    onDismiss: () -> Unit
) {
    var tempSelected by remember { mutableStateOf(currentLanguage) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .clip(RoundedCornerShape(24.dp))
                .background(DevSurfaceDark)
                .border(1.dp, DevBorderGlass, RoundedCornerShape(24.dp))
                .padding(24.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header Icon with Glow
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.linearGradient(
                                listOf(DevCyan.copy(alpha = 0.2f), DevPurple.copy(alpha = 0.2f))
                            )
                        )
                        .border(1.dp, DevBorderGlass, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Language,
                        contentDescription = "Language",
                        tint = DevCyan,
                        modifier = Modifier.size(28.dp)
                    )
                }

                // Title & Subtitle
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = Strings.langDialogTitle(tempSelected),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = Strings.langDialogSubtitle(tempSelected),
                        fontSize = 12.sp,
                        color = DevTextSecondary,
                        textAlign = TextAlign.Center,
                        lineHeight = 17.sp
                    )
                }

                // Language Option Cards
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Option 1: বাংলা
                    val isBnSelected = tempSelected == AppLanguage.BANGLA
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(18.dp))
                            .clickable { tempSelected = AppLanguage.BANGLA }
                            .border(
                                width = if (isBnSelected) 2.dp else 1.dp,
                                color = if (isBnSelected) DevCyan else DevBorderGlass,
                                shape = RoundedCornerShape(18.dp)
                            ),
                        color = if (isBnSelected) DevCyan.copy(alpha = 0.12f) else DevCardGlass
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            Text(
                                text = "🇧🇩",
                                fontSize = 28.sp
                            )

                            Column(modifier = Modifier.weight(1f)) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Text(
                                        text = "বাংলা (Bengali)",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isBnSelected) DevCyan else Color.White
                                    )
                                    if (isBnSelected) {
                                        Surface(
                                            color = DevCyan,
                                            shape = RoundedCornerShape(4.dp)
                                        ) {
                                            Text(
                                                text = "সক্রিয়",
                                                fontSize = 9.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color.Black,
                                                modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                                            )
                                        }
                                    }
                                }
                                Spacer(modifier = Modifier.height(3.dp))
                                Text(
                                    text = Strings.langBanglaDesc(tempSelected),
                                    fontSize = 11.sp,
                                    color = DevTextSecondary,
                                    lineHeight = 15.sp
                                )
                            }

                            RadioButton(
                                selected = isBnSelected,
                                onClick = { tempSelected = AppLanguage.BANGLA },
                                colors = RadioButtonDefaults.colors(
                                    selectedColor = DevCyan,
                                    unselectedColor = DevTextMuted
                                )
                            )
                        }
                    }

                    // Option 2: English
                    val isEnSelected = tempSelected == AppLanguage.ENGLISH
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(18.dp))
                            .clickable { tempSelected = AppLanguage.ENGLISH }
                            .border(
                                width = if (isEnSelected) 2.dp else 1.dp,
                                color = if (isEnSelected) DevPurple else DevBorderGlass,
                                shape = RoundedCornerShape(18.dp)
                            ),
                        color = if (isEnSelected) DevPurple.copy(alpha = 0.12f) else DevCardGlass
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            Text(
                                text = "🌐",
                                fontSize = 28.sp
                            )

                            Column(modifier = Modifier.weight(1f)) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Text(
                                        text = "English",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isEnSelected) DevPurple else Color.White
                                    )
                                    if (isEnSelected) {
                                        Surface(
                                            color = DevPurple,
                                            shape = RoundedCornerShape(4.dp)
                                        ) {
                                            Text(
                                                text = "ACTIVE",
                                                fontSize = 9.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color.White,
                                                modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                                            )
                                        }
                                    }
                                }
                                Spacer(modifier = Modifier.height(3.dp))
                                Text(
                                    text = Strings.langEnglishDesc(tempSelected),
                                    fontSize = 11.sp,
                                    color = DevTextSecondary,
                                    lineHeight = 15.sp
                                )
                            }

                            RadioButton(
                                selected = isEnSelected,
                                onClick = { tempSelected = AppLanguage.ENGLISH },
                                colors = RadioButtonDefaults.colors(
                                    selectedColor = DevPurple,
                                    unselectedColor = DevTextMuted
                                )
                            )
                        }
                    }
                }

                // Action Continue Button
                Button(
                    onClick = {
                        onLanguageSelected(tempSelected)
                        onDismiss()
                    },
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = Color.Black
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                ) {
                    Text(
                        text = Strings.langContinue(tempSelected),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }
            }
        }
    }
}
