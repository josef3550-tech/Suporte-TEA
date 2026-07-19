package com.example.data

data class CommunicationCard(
    val emoji: String,
    val text: String,
    val category: String
)

object CommunicationData {
    val categories = listOf(
        "Emoções",
        "Necessidades",
        "Ações",
        "Escola",
        "Saúde"
    )

    val cards = listOf(
        // Emoções
        CommunicationCard("😊", "Estou feliz.", "Emoções"),
        CommunicationCard("😢", "Estou triste.", "Emoções"),
        CommunicationCard("😡", "Estou com raiva.", "Emoções"),
        CommunicationCard("😨", "Estou com medo.", "Emoções"),
        CommunicationCard("😴", "Estou cansado.", "Emoções"),
        CommunicationCard("😖", "Estou com dor.", "Emoções"),
        CommunicationCard("😍", "Gostei.", "Emoções"),
        CommunicationCard("😕", "Não gostei.", "Emoções"),

        // Necessidades
        CommunicationCard("💧", "Quero água.", "Necessidades"),
        CommunicationCard("🍽", "Quero comer.", "Necessidades"),
        CommunicationCard("🚽", "Quero ir ao banheiro.", "Necessidades"),
        CommunicationCard("🛏", "Quero dormir.", "Necessidades"),
        CommunicationCard("🤗", "Quero um abraço.", "Necessidades"),
        CommunicationCard("🧸", "Quero brincar.", "Necessidades"),
        CommunicationCard("📱", "Quero meu brinquedo.", "Necessidades"),
        CommunicationCard("🏃", "Quero passear.", "Necessidades"),

        // Ações
        CommunicationCard("✔", "Sim.", "Ações"),
        CommunicationCard("✖", "Não.", "Ações"),
        CommunicationCard("⏳", "Espere.", "Ações"),
        CommunicationCard("🙋", "Me ajude.", "Ações"),
        CommunicationCard("🔇", "Pare.", "Ações"),
        CommunicationCard("▶", "Continue.", "Ações"),
        CommunicationCard("👀", "Olhe para mim.", "Ações"),
        CommunicationCard("🙏", "Obrigado.", "Ações"),

        // Escola
        CommunicationCard("✏", "Quero escrever.", "Escola"),
        CommunicationCard("📖", "Quero ler.", "Escola"),
        CommunicationCard("🎨", "Quero desenhar.", "Escola"),
        CommunicationCard("🎵", "Quero música.", "Escola"),
        CommunicationCard("👩‍🏫", "Quero falar com a professora.", "Escola"),

        // Saúde
        CommunicationCard("🤒", "Estou passando mal.", "Saúde"),
        CommunicationCard("🤕", "Minha cabeça dói.", "Saúde"),
        CommunicationCard("🤢", "Estou enjoado.", "Saúde"),
        CommunicationCard("🌡", "Estou com febre.", "Saúde")
    )
}
