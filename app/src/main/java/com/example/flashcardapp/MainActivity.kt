package com.example.flashcardapp

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
 override fun onCreate(savedInstanceState: Bundle?) {
  super.onCreate(savedInstanceState)
  setContent {
   MaterialTheme {
    QuizFlashcardApp()
   }
  }
 }
}

@SuppressLint("ContextCastToActivity")
@Composable
fun QuizFlashcardApp() {
 val questions = listOf(
  "Columbus was the first European to sail to the Americas" to true,
  "The Battle of Agincourt was fought by Henry II" to false,
  "The Hundred Years' War lasted less than 100 years" to false,
  "The Nazis invaded Greece during WWII" to true,
  "The Great Wall of China is visible from space." to false
 )

 var currentIndex by rememberSaveable { mutableStateOf(-1) }
 var score by rememberSaveable { mutableStateOf(0) }
 var feedback by rememberSaveable { mutableStateOf("") }
 var showResult by rememberSaveable { mutableStateOf(false) }
 var showReview by rememberSaveable { mutableStateOf(false) }
 var hasAnswered by remember { mutableStateOf(false) }
 val userAnswers = remember { mutableStateListOf<Boolean>() }

 val activity = (LocalContext.current as? Activity)

 Column(
  modifier = Modifier
   .background(Color(0xFFE3F2FD)) // Light blue background
   .fillMaxSize()
   .padding(24.dp),
  verticalArrangement = Arrangement.Center,
  horizontalAlignment = Alignment.CenterHorizontally
 ) {
  when {
   showReview -> {
    Text("📖 Review Answers", fontSize = 24.sp)
    Spacer(modifier = Modifier.height(16.dp))

    questions.forEachIndexed { i, pair ->
     val correctAnswer = pair.second
     val userAnswer = if (i < userAnswers.size) userAnswers[i] else null
     val result = if (userAnswer == correctAnswer) "✔ Correct" else "✘ Incorrect"
     Text("${i + 1}. ${pair.first}", fontSize = 18.sp)
     Text("Correct: $correctAnswer – $result", fontSize = 16.sp)
     Spacer(modifier = Modifier.height(12.dp))
    }

    Spacer(modifier = Modifier.height(16.dp))
    Button(onClick = {
     currentIndex = -1
     score = 0
     feedback = ""
     userAnswers.clear()
     showReview = false
    }) {
     Text("Back to Start")
    }
    Spacer(modifier = Modifier.height(12.dp))
    Button(onClick = { activity?.finish() }) {
     Text("Exit")
    }
   }

   showResult -> {
    Text("🏁 Quiz Completed!", fontSize = 24.sp)
    Spacer(modifier = Modifier.height(12.dp))
    Text("Your Score: $score / ${questions.size}", fontSize = 20.sp)
    Spacer(modifier = Modifier.height(8.dp))
    Text(
     if (score >= 3) "🎯 Great job!" else "📘 Keep practicing!",
     fontSize = 18.sp,
     textAlign = TextAlign.Center
    )
    Spacer(modifier = Modifier.height(24.dp))
    Button(onClick = {
     showReview = true
    }) {
     Text("Review Answers")
    }
    Spacer(modifier = Modifier.height(12.dp))
    Button(onClick = {
     currentIndex = -1
     score = 0
     feedback = ""
     userAnswers.clear()
     showResult = false
    }) {
     Text("Restart")
    }
    Spacer(modifier = Modifier.height(12.dp))
    Button(onClick = { activity?.finish() }) {
     Text("Exit")
    }
   }

   currentIndex == -1 -> {
    Text("🎉 Welcome to History Quiz!", fontSize = 24.sp)
    Spacer(modifier = Modifier.height(24.dp))
    Button(onClick = {
     currentIndex = 0
     feedback = ""
     hasAnswered = false
    }) {
     Text("Start Quiz")
    }
    Spacer(modifier = Modifier.height(12.dp))
    Button(onClick = { activity?.finish() }) {
     Text("Exit")
    }
   }

   else -> {
    Text("Question ${currentIndex + 1}", fontSize = 18.sp)
    Spacer(modifier = Modifier.height(8.dp))
    Text(
     questions[currentIndex].first,
     fontSize = 20.sp,
     textAlign = TextAlign.Center
    )
    Spacer(modifier = Modifier.height(24.dp))

    Row(
     modifier = Modifier.fillMaxWidth(),
     horizontalArrangement = Arrangement.SpaceEvenly
    ) {
     Button(onClick = {
      if (!hasAnswered) {
       val userAnswer = true
       userAnswers.add(userAnswer)
       if (userAnswer == questions[currentIndex].second) {
        score++
        feedback = "Correct!"
       } else {
        feedback = "Incorrect"
       }
       hasAnswered = true
      }
     }) {
      Text("True")
     }

     Button(onClick = {
      if (!hasAnswered) {
       val userAnswer = false
       userAnswers.add(userAnswer)
       if (userAnswer == questions[currentIndex].second) {
        score++
        feedback = "Correct!"
       } else {
        feedback = "Incorrect"
       }
       hasAnswered = true
      }
     }) {
      Text("False")
     }
    }

    Spacer(modifier = Modifier.height(16.dp))
    Text(feedback, fontSize = 18.sp)

    if (hasAnswered) {
     Spacer(modifier = Modifier.height(12.dp))
     Button(onClick = {
      hasAnswered = false
      feedback = ""
      goToNextQuestion(currentIndex, questions.size) {
       currentIndex = it
       showResult = it >= questions.size
      }
     }) {
      Text("Next")
     }
    }
   }
  }
 }
}

fun goToNextQuestion(current: Int, total: Int, update: (Int) -> Unit) {
 update(current + 1)
}