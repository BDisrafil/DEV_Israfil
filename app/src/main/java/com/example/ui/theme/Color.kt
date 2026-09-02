package com.example.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// Premium SaaS Landing Palette
val DevBgDark = Color(0xFF050505) // Deep SaaS Onyx
val DevSurfaceDark = Color(0xFF0A0A0A) // Header & Nav Surface
val DevCardSurface = Color(0xFF0D0D0D) // Card container
val DevCardGlass = Color(0x0DFFFFFF) // bg-white/5 (~5% opacity white)
val DevCardGlassHover = Color(0x1AFFFFFF) // bg-white/10 (~10% opacity white)
val DevBorderGlass = Color(0x1AFFFFFF) // border-white/10 (~10% opacity white)
val DevBorderMuted = Color(0x0FFFFFFF) // Subtle border (~6% opacity white)

// Accent Colors
val DevCyan = Color(0xFF06B6D4) // cyan-500
val DevCyanLight = Color(0xFF67E8F9) // cyan-300
val DevCyanGlow = Color(0x1A06B6D4) // cyan-500/10
val DevBlue = Color(0xFF38BDF8)
val DevPurple = Color(0xFF9333EA) // purple-600
val DevPurpleGlow = Color(0x1A9333EA) // purple-600/10
val DevIndigo = Color(0xFF6366F1)
val DevEmerald = Color(0xFF10B981)
val DevRose = Color(0xFFF43F5E)
val DevAmber = Color(0xFFF59E0B)

// Typography & Text
val DevTextPrimary = Color(0xFFF1F5F9) // slate-100
val DevTextSecondary = Color(0xFF94A3B8) // slate-400
val DevTextMuted = Color(0xFF64748B) // slate-500

// SaaS Landing Gradients
val DevGradientHero = Brush.linearGradient(
    colors = listOf(Color(0xFF06B6D4), Color(0xFF9333EA))
)

val DevGradientHeadline = Brush.horizontalGradient(
    colors = listOf(Color(0xFFFFFFFF), Color(0xFF94A3B8))
)

val DevGradientCardBorder = Brush.linearGradient(
    colors = listOf(Color(0x3306B6D4), Color(0x229333EA), Color(0x1AFFFFFF))
)

val DevGradientButton = Brush.horizontalGradient(
    colors = listOf(Color(0xFF06B6D4), Color(0xFF9333EA))
)

val DevGradientThumbnailBox = Brush.linearGradient(
    colors = listOf(Color(0x4D06B6D4), Color(0x4D9333EA))
)

val DevAmbientCyanGlow = Brush.radialGradient(
    colors = listOf(Color(0x2E06B6D4), Color(0x00000000))
)

val DevAmbientPurpleGlow = Brush.radialGradient(
    colors = listOf(Color(0x2E9333EA), Color(0x00000000))
)

