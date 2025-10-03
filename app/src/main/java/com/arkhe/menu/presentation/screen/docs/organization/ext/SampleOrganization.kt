@file:Suppress("SpellCheckingInspection")

package com.arkhe.menu.presentation.screen.docs.organization.ext

import com.arkhe.menu.R

data class Organization(
    val id: Int,
    val name: String,
    val division: String,
    val position: String,
    val fotoRes: Int,
    val guideFor: String,
    val email: String,
    val whatsapp: String,
    val instagram: String,
    val bio: String
)

val sampleOrganizations = listOf(
    Organization(
        id = 1,
        name = "Didik Muttaqien",
        division = "BROD",
        fotoRes = R.drawable.ic_image,
        position = "Director",
        guideFor = "Sobat Jalan",
        email = "andi.pratama@tripkeun.com",
        whatsapp = "+62 812-3456-7890",
        instagram = "@didikmuttaqien",
        bio = "Passionate UI/UX Designer yang berfokus pada user experience dan desain yang menarik. Memiliki pengalaman dalam berbagai proyek aplikasi mobile dan web."
    ),
    Organization(
        id = 2,
        name = "Sari Indah",
        division = "Frontend Developer React & Vue",
        fotoRes = R.drawable.ic_image,
        position = "Frontend Developer",
        guideFor = "4 tahun",
        email = "sari.indah@tripkeun.com",
        whatsapp = "+62 813-4567-8901",
        instagram = "@sariindah",
        bio = "Frontend Developer yang ahli dalam React dan Vue.js. Senang mengimplementasikan desain menjadi kode yang clean dan responsive."
    ),
    Organization(
        id = 3,
        name = "Budi Santoso",
        division = "Backend Developer Java & Spring",
        fotoRes = R.drawable.ic_image,
        position = "Backend Developer",
        guideFor = "6 tahun",
        email = "budi.santoso@tripkeun.com",
        whatsapp = "+62 814-5678-9012",
        instagram = "@budisantoso",
        bio = "Backend Developer dengan keahlian dalam Java, Spring Boot, dan database management. Berpengalaman dalam membangun API yang scalable."
    ),
    Organization(
        id = 4,
        name = "Lisa Maharani",
        division = "Project Manager & Scrum Master",
        fotoRes = R.drawable.ic_image,
        position = "Project Manager",
        guideFor = "7 tahun",
        email = "lisa.maharani@tripkeun.com",
        whatsapp = "+62 815-6789-0123",
        instagram = "@lisamaharani",
        bio = "Berpengalaman dalam mengelola proyek teknologi dengan metodologi Agile dan Scrum. Memastikan delivery yang tepat waktu dan berkualitas."
    ),
    Organization(
        id = 5,
        name = "Raka Wijaya",
        division = "Mobile Developer Android & iOS",
        fotoRes = R.drawable.ic_image,
        position = "Mobile Developer",
        guideFor = "4 tahun",
        email = "raka.wijaya@tripkeun.com",
        whatsapp = "+62 816-7890-1234",
        instagram = "@rakawijaya",
        bio = "Mobile Developer yang mahir dalam pengembangan aplikasi Android native dan iOS. Fokus pada performance dan user experience yang optimal."
    ),
    Organization(
        id = 6,
        name = "Dina Pratiwi",
        division = "QA Engineer & Tester",
        fotoRes = R.drawable.ic_image,
        position = "QA Engineer",
        guideFor = "3 tahun",
        email = "dina.pratiwi@tripkeun.com",
        whatsapp = "+62 817-8901-2345",
        instagram = "@dinapratiwi",
        bio = "Quality Assurance Engineer yang detail dalam melakukan testing manual dan automation. Memastikan produk bebas dari bug sebelum release."
    ),
    Organization(
        id = 7,
        name = "Agus Setiawan",
        division = "DevOps Engineer & Cloud Specialist",
        fotoRes = R.drawable.ic_image,
        position = "DevOps Engineer",
        guideFor = "5 tahun",
        email = "agus.setiawan@tripkeun.com",
        whatsapp = "+62 818-9012-3456",
        instagram = "@agussetiawan",
        bio = "DevOps Engineer dengan keahlian dalam AWS, Docker, dan Kubernetes. Fokus pada automation dan infrastructure yang reliable."
    ),
    Organization(
        id = 8,
        name = "Maya Sari",
        division = "Data Analyst & Business Intelligence",
        fotoRes = R.drawable.ic_image,
        position = "Data Analyst",
        guideFor = "3 tahun",
        email = "maya.sari@tripkeun.com",
        whatsapp = "+62 819-0123-4567",
        instagram = "@mayasari",
        bio = "Data Analyst yang ahli dalam menganalisis data bisnis dan membuat insights yang actionable. Berpengalaman dengan tools seperti Python, SQL, dan Tableau."
    )
)