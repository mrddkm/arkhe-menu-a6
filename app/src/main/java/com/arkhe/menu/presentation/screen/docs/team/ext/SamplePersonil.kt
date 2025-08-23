@file:Suppress("SpellCheckingInspection")

package com.arkhe.menu.presentation.screen.docs.team.ext

import com.arkhe.menu.R

data class Personil(
    val id: Int,
    val nama: String,
    val deskripsi: String,
    val fotoRes: Int, // Resource ID untuk foto
    val posisi: String,
    val pengalaman: String,
    val email: String,
    val telepon: String,
    val bio: String
)

val samplePersonil = listOf(
    Personil(
        id = 1,
        nama = "Andi Pratama",
        deskripsi = "UI/UX Designer dengan 5 tahun pengalaman",
        fotoRes = R.drawable.ic_launcher_foreground, // Ganti dengan resource foto yang sesuai
        posisi = "UI/UX Designer",
        pengalaman = "5 tahun",
        email = "andi.pratama@tripkeun.com",
        telepon = "+62 812-3456-7890",
        bio = "Passionate UI/UX Designer yang berfokus pada user experience dan desain yang menarik. Memiliki pengalaman dalam berbagai proyek aplikasi mobile dan web."
    ),
    Personil(
        id = 2,
        nama = "Sari Indah",
        deskripsi = "Frontend Developer React & Vue",
        fotoRes = R.drawable.ic_launcher_foreground,
        posisi = "Frontend Developer",
        pengalaman = "4 tahun",
        email = "sari.indah@tripkeun.com",
        telepon = "+62 813-4567-8901",
        bio = "Frontend Developer yang ahli dalam React dan Vue.js. Senang mengimplementasikan desain menjadi kode yang clean dan responsive."
    ),
    Personil(
        id = 3,
        nama = "Budi Santoso",
        deskripsi = "Backend Developer Java & Spring",
        fotoRes = R.drawable.ic_launcher_foreground,
        posisi = "Backend Developer",
        pengalaman = "6 tahun",
        email = "budi.santoso@tripkeun.com",
        telepon = "+62 814-5678-9012",
        bio = "Backend Developer dengan keahlian dalam Java, Spring Boot, dan database management. Berpengalaman dalam membangun API yang scalable."
    ),
    Personil(
        id = 4,
        nama = "Lisa Maharani",
        deskripsi = "Project Manager & Scrum Master",
        fotoRes = R.drawable.ic_launcher_foreground,
        posisi = "Project Manager",
        pengalaman = "7 tahun",
        email = "lisa.maharani@tripkeun.com",
        telepon = "+62 815-6789-0123",
        bio = "Berpengalaman dalam mengelola proyek teknologi dengan metodologi Agile dan Scrum. Memastikan delivery yang tepat waktu dan berkualitas."
    ),
    Personil(
        id = 5,
        nama = "Raka Wijaya",
        deskripsi = "Mobile Developer Android & iOS",
        fotoRes = R.drawable.ic_launcher_foreground,
        posisi = "Mobile Developer",
        pengalaman = "4 tahun",
        email = "raka.wijaya@tripkeun.com",
        telepon = "+62 816-7890-1234",
        bio = "Mobile Developer yang mahir dalam pengembangan aplikasi Android native dan iOS. Fokus pada performance dan user experience yang optimal."
    ),
    Personil(
        id = 6,
        nama = "Dina Pratiwi",
        deskripsi = "QA Engineer & Tester",
        fotoRes = R.drawable.ic_launcher_foreground,
        posisi = "QA Engineer",
        pengalaman = "3 tahun",
        email = "dina.pratiwi@tripkeun.com",
        telepon = "+62 817-8901-2345",
        bio = "Quality Assurance Engineer yang detail dalam melakukan testing manual dan automation. Memastikan produk bebas dari bug sebelum release."
    ),
    Personil(
        id = 7,
        nama = "Agus Setiawan",
        deskripsi = "DevOps Engineer & Cloud Specialist",
        fotoRes = R.drawable.ic_launcher_foreground,
        posisi = "DevOps Engineer",
        pengalaman = "5 tahun",
        email = "agus.setiawan@tripkeun.com",
        telepon = "+62 818-9012-3456",
        bio = "DevOps Engineer dengan keahlian dalam AWS, Docker, dan Kubernetes. Fokus pada automation dan infrastructure yang reliable."
    ),
    Personil(
        id = 8,
        nama = "Maya Sari",
        deskripsi = "Data Analyst & Business Intelligence",
        fotoRes = R.drawable.ic_launcher_foreground,
        posisi = "Data Analyst",
        pengalaman = "3 tahun",
        email = "maya.sari@tripkeun.com",
        telepon = "+62 819-0123-4567",
        bio = "Data Analyst yang ahli dalam menganalisis data bisnis dan membuat insights yang actionable. Berpengalaman dengan tools seperti Python, SQL, dan Tableau."
    )
)