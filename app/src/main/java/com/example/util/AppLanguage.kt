package com.example.util

import android.content.Context
import android.content.SharedPreferences

enum class AppLanguage(val code: String, val displayName: String, val nativeName: String, val flagEmoji: String) {
    BANGLA("bn", "Bangla", "বাংলা", "🇧🇩"),
    ENGLISH("en", "English", "English", "🌐")
}

class LanguagePreferences(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("dev_israfil_prefs", Context.MODE_PRIVATE)

    var currentLanguage: AppLanguage
        get() {
            val code = prefs.getString("selected_language", null)
            return if (code == "bn") AppLanguage.BANGLA else if (code == "en") AppLanguage.ENGLISH else AppLanguage.BANGLA
        }
        set(value) {
            prefs.edit().putString("selected_language", value.code).apply()
        }

    var hasSelectedLanguageOnce: Boolean
        get() = prefs.getBoolean("has_selected_lang_once", false)
        set(value) = prefs.edit().putBoolean("has_selected_lang_once", value).apply()
}

object Strings {
    fun appTitle(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "DEV_Israfil" else "DEV_Israfil"
    fun tagline(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "কোড • ক্রিয়েট • ডাউনলোড" else "Code • Create • Download"
    fun heroHeadline(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "সেরা কোড তৈরি করুন।\nসহজে ডাউনলোড করুন।" else "Build Better.\nCode Smarter."
    fun heroSubtitle(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "আপনার পরবর্তী প্রজেক্টের জন্য প্রিমিয়াম সোর্স কোড ও ফ্রি স্ক্রিপ্ট ডাউনলোড করুন।" else "Discover premium source codes for your next project."
    fun searchPlaceholder(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "প্রজেক্ট বা সোর্স কোড খুঁজুন..." else "Search resources..."
    
    // Navigation
    fun navHome(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "হোম" else "Home"
    fun navBrowse(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "ব্রাউজ" else "Browse"
    fun navProjects(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "সব প্রজেক্ট" else "All Projects"
    fun navCategories(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "ক্যাটাগরি" else "Categories"
    fun navAbout(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "সম্পর্কে" else "About"
    fun navContact(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "যোগাযোগ" else "Contact"
    fun navAdmin(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "অ্যাডমিন" else "Admin"
    fun navLanguage(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "ভাষা নির্বাচন" else "Language"
    
    // Sections
    fun quickAccess(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "কুইক অ্যাক্সেস" else "QUICK ACCESS"
    fun viewAll(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "সব দেখুন" else "View All"
    fun featuredProject(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "ফিচার্ড প্রজেক্ট" else "FEATURED PROJECT"
    fun newRelease(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "নতুন রিলিজ" else "NEW RELEASE"
    fun viewProject(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "প্রজেক্ট দেখুন" else "View Project"
    fun downloadZip(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "জিপ ডাউনলোড" else "Download ZIP"
    fun popularProjects(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "জনপ্রিয় প্রজেক্টসমূহ" else "Popular Projects"
    fun latestReleases(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "সর্বশেষ প্রজেক্টসমূহ" else "Latest Releases"
    fun noProjectsFound(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "কোনো প্রজেক্ট পাওয়া যায়নি" else "No projects found"
    
    // Stats
    fun statProjects(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "প্রজেক্ট" else "Projects"
    fun statDownloads(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "ডাউনলোড" else "Downloads"
    fun statCategories(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "ক্যাটাগরি" else "Categories"
    
    // Projects Screen
    fun projectsLibraryTitle(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "কোডিং প্রজেক্ট লাইব্রেরি" else "Coding Projects Library"
    fun projectsLibrarySubtitle(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "প্রিমিয়াম সোর্স কোড ব্রাউজ ও ডাউনলোড করুন" else "Browse, preview and download production-ready code repositories."
    fun filterPlaceholder(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "টাইটেল, ট্যাগ অথবা ক্যাটাগরি দিয়ে খুঁজুন..." else "Filter by title, tag or category..."
    fun allCategoriesLabel(lang: AppLanguage, count: Int) = if (lang == AppLanguage.BANGLA) "সকল ক্যাটাগরি ($count)" else "All Categories ($count)"
    fun projectsAvailable(lang: AppLanguage, count: Int) = if (lang == AppLanguage.BANGLA) "$count টি প্রজেক্ট পাওয়া গেছে" else "$count projects available"

    // Detail Screen
    fun livePreview(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "লাইভ প্রিভিউ" else "Live Preview"
    fun exploreCode(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "কোড দেখুন" else "Explore Code"
    fun watchVideo(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "ভিডিও টিউটোরিয়াল" else "Watch Tutorial"
    fun projectOverview(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "প্রজেক্ট ওভারভিউ" else "Project Overview"
    fun techSpecs(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "প্রজেক্টের বিবরণ" else "Technical Specifications"
    fun sourceFiles(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "অন্তর্ভুক্ত ফাইলসমূহ" else "Included Source Files"
    fun relatedProjects(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "অনুরূপ প্রজেক্ট" else "Related Projects"
    fun downloadCompleteZip(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "সম্পূর্ণ জিপ ফাইল ডাউনলোড করুন" else "Download Complete ZIP"
    fun fileSize(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "ফাইলের সাইজ" else "File Size"
    fun version(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "ভার্সন" else "Version"
    fun publishedOn(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "প্রকাশিত" else "Published"
    
    // Sorting & Filters
    fun sortBy(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "বাছাই করুন" else "Sort By"
    fun sortPopular(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "সর্বোচ্চ ডাউনলোড" else "Most Downloaded"
    fun sortNewest(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "নতুন প্রকাশিত" else "Newest First"
    fun sortName(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "নামানুসারে (A-Z)" else "Alphabetical"
    fun allFilter(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "সব প্রজেক্ট" else "All"
    
    // Modals
    fun downloadModalTitle(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "প্রজেক্ট জিপ তৈরি ও ডাউনলোড হচ্ছে" else "Generating & Downloading Project ZIP"
    fun downloadCompleted(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "ডাউনলোড সম্পন্ন হয়েছে!" else "Download Ready!"
    fun downloadReadyDesc(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "আপনার জিপ ফাইল মেমোরিতে সেভ হয়েছে। শেয়ার বা ওপেন করতে পারেন।" else "Your ZIP file has been compiled with README instructions and is ready to use."
    fun openZip(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "ফাইল ওপেন / শেয়ার করুন" else "Open / Share ZIP"
    fun close(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "বন্ধ করুন" else "Close"
    
    // Language Dialog
    fun langDialogTitle(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "ভাষা নির্বাচন করুন" else "Select Language"
    fun langDialogSubtitle(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "অ্যাপটি বাংলা অথবা ইংরেজিতে ব্যবহার করতে নিচের যেকোনো একটি ভাষা বেছে নিন" else "Choose your preferred language for the best experience"
    fun langBanglaDesc(lang: AppLanguage) = "সহজে বাংলা ভাষায় প্রজেক্ট ব্রাউজ ও সোর্স কোড ডাউনলোড করুন"
    fun langEnglishDesc(lang: AppLanguage) = "Explore and download source codes with English interface"
    fun langContinue(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "এগিয়ে যান" else "Continue"
    fun langSwitchSuccess(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "ভাষা পরিবর্তন করা হয়েছে: বাংলা" else "Language changed to English"
    
    // Admin & Drawer
    fun adminPortal(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "অ্যাডমিন পোর্টাল" else "Admin Portal"
    fun adminDashboard(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "অ্যাডমিন ড্যাশবোর্ড" else "Admin Dashboard"
    fun uploadNewProject(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "+ নতুন প্রজেক্ট আপলোড" else "+ Upload Project"
    fun adminLogout(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "অ্যাডমিন লগআউট" else "Admin Logout"
    fun mainNavHeader(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "প্রধান মেনু" else "MAIN NAVIGATION"
    fun adminNavHeader(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "অ্যাডমিন প্যানেল" else "ADMINISTRATION"
    fun languageNavHeader(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "ভাষা / LANGUAGE" else "LANGUAGE / ভাষা"
    
    // About & Contact
    fun aboutTitle(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "DEV_Israfil সম্পর্কে" else "About DEV_Israfil"
    fun aboutDesc(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "DEV_Israfil হলো ডেভেলপারদের জন্য একটি প্রিমিয়াম সোর্স কোড ও প্রজেক্ট হাব। এখানে যেকোনো প্রোগ্রামার ও শিক্ষার্থী সহজে HTML, CSS, JavaScript, React, Python, PHP ও Android প্রজেক্ট বিনামূল্যে বা এক ক্লিকে জিপ ফাইলে ডাউনলোড করতে পারেন।" else "DEV_Israfil is a premier open-source code and development platform dedicated to providing students, learners, and professional creators with clean, high-impact web and software source codes."
    fun contactUs(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "যোগাযোগ করুন" else "Contact Us"
    fun contactSubtitle(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "কোনো সাহায্য, কাস্টম প্রজেক্ট রিকোয়েস্ট বা প্রশ্নের জন্য আমাদের সাথে যোগাযোগ করুন।" else "Have a question, request a project, or want to collaborate? Send us a message directly."
    fun yourName(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "আপনার নাম" else "Your Name"
    fun yourEmail(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "আপনার ইমেইল" else "Your Email"
    fun yourMessage(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "আপনার বার্তা / প্রজেক্ট রিকোয়েস্ট" else "Your Message"
    fun sendMessage(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "বার্তা পাঠান" else "Send Message"
    fun officialChannels(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "অফিশিয়াল চ্যানেলসমূহ" else "Official Channels"
}
