package com.ippon.geminitraveler.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import java.util.StringJoiner
import kotlin.random.Random

/**
 * A composable function that displays a text with a typewriter-like effect, revealing characters in chunks.
 *
 * @param text The input text to be displayed with the typewriter effect.
 * @param minDelayInMillis The minimum delay in milliseconds between revealing character chunks, defaults to 10ms.
 * @param maxDelayInMillis The maximum delay in milliseconds between revealing character chunks, defaults to 50ms.
 * @param minCharacterChunk The minimum number of characters to reveal at once, defaults to 1.
 * @param maxCharacterChunk The maximum number of characters to reveal at once, defaults to 5.
 * @param onEffectCompleted A callback function invoked when the entire text has been revealed.
 * @param displayTextComposable A composable function that receives the text to display with the typewriter effect.
 *
 * @throws IllegalArgumentException if [minDelayInMillis] is greater than [maxDelayInMillis].
 * @throws IllegalArgumentException if [minCharacterChunk] is greater than [maxCharacterChunk].
 */
@Composable
fun TypewriterTextEffect(
    modifier: Modifier = Modifier,
    text: String,
    minDelayInMillis: Long = 10,
    maxDelayInMillis: Long = 50,
    minCharacterChunk: Int = 1,
    maxCharacterChunk: Int = 5,
    onEffectCompleted: () -> Unit = {},
    displayTextComposable: @Composable (displayedText: String) -> Unit
) {
    val scrollState = rememberScrollState()

    // Initialize and remember the displayedText
    var currentText by remember(text) { mutableStateOf("") }
    val beginText by remember {
        mutableStateOf(StringJoiner(""))
    }

    LaunchedEffect(currentText) {
        scrollState.animateScrollTo(Int.MAX_VALUE)
    }

    // Call the displayTextComposable with the current displayedText value

    Column(
        modifier = modifier.verticalScroll(scrollState)
    ) {
        displayTextComposable("$beginText$currentText")
    }

    // Launch the effect to update the displayedText value over time
    LaunchedEffect(text) {
        val textLength = text.length
        var endIndex = 0

        while (endIndex < textLength) {
            endIndex = minOf(
                endIndex + Random.nextInt(minCharacterChunk, maxCharacterChunk + 1),
                textLength
            )
            currentText = text.substring(startIndex = 0, endIndex = endIndex)
            delay(Random.nextLong(minDelayInMillis, maxDelayInMillis))
        }
        beginText.add(currentText)
        onEffectCompleted()
    }
}

/**
 * An example composable that uses the TypewriterTextEffect function.
 */
@Composable
@PreviewScreenSizes
@Preview
private fun TypewriterTextEffectPreview() {
    var index by remember {
        mutableIntStateOf(0)
    }
    val texts = remember {
        mutableStateListOf(
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
        )
    }

    LaunchedEffect(Unit) {
        delay(250)
        texts.add(
            "\n\nA composable function that displays a text with a typewriter-like effect, revealing characters in chunks."
        )
        delay(250)
        texts.add(
            "\n\nOne Piece (ワンピース, Wan Pīsu?) est une série de shōnen mangas créée par Eiichirō Oda. Elle est prépubliée depuis le 22 juillet 1997 dans le magazine hebdomadaire Weekly Shōnen Jump, puis regroupée en tankōbon aux éditions Shūeisha depuis le 24 décembre 1997. 107 tomes sont commercialisés au Japon en novembre 2023. La version française est publiée en volumes reliés depuis le 1er septembre 2000 par Glénat, et 106 volumes sont commercialisés en décembre 2023. Depuis le 26 septembre 2021, la version française est prépubliée simultanément avec la version japonaise sur les plates-formes en ligne Manga Plus et Glénat Manga Max.\n" +
                    "\n" +
                    "L'histoire suit les aventures de Monkey D. Luffy, un garçon dont le corps a acquis les propriétés du caoutchouc après avoir mangé par inadvertance un fruit du démon. Avec son équipage de pirates, appelé l'équipage de Chapeau de paille, Luffy explore Grand Line à la recherche du trésor ultime connu sous le nom de « One Piece » afin de devenir le prochain roi des pirates."
        )
        delay(250)
        texts.add(
            "\n\nNaruto (ナルト?) est un shōnen manga écrit et dessiné par Masashi Kishimoto. Naruto a été prépublié dans l'hebdomadaire Weekly Shōnen Jump de l'éditeur Shūeisha entre septembre 1999 et novembre 2014. La série a été compilée en 72 tomes. La version française du manga est publiée par Kana entre mars 2002 et novembre 2016.\n" +
                    "\n" +
                    "À la suite de son succès sous forme de manga, une adaptation en anime est réalisée par les studios Pierrot et Aniplex et est diffusée sur TV Tokyo depuis le 3 octobre 2002. Une seconde partie du récit a aussi vu le jour et a été renommée Naruto Shippuden lors de son adaptation. La série animée est diffusée en France depuis le 2 janvier 2006 sur Game One ainsi que sur NT1 et sur Cartoon Network depuis la rentrée 2007. En Belgique, elle est diffusée sur Club RTL depuis la rentrée 2008.\n" +
                    "\n" +
                    "Game One diffuse aussi depuis le 5 septembre 2008 la seconde série : Naruto Shippuden. Les épisodes sont également proposés en version originale sous-titrée en français en simulcast sur J-One, et sur les plates-formes Anime Digital Network, Netflix et Prime Video.\n" +
                    "\n" +
                    "Avec plus de 250 millions de copies éditées, Naruto est l'un des mangas les plus vendus de l'histoire et l'une des bandes dessinées les plus vendues au monde. En raison de son succès, des récits inédits sont également produits régulièrement sous forme de longs métrages d'animation entre 2004 et 2015."
        )
    }
    // Use the TypewriterTextEffect composable with a Text composable to display the text with the typewriter effect
    Box(
        Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        TypewriterTextEffect(
            text = texts[index],
            onEffectCompleted = {
                index++
            },
        ) { displayedText ->
            BasicText(
                text = displayedText,
                style = MaterialTheme.typography.titleSmall,
            )
        }
    }
}