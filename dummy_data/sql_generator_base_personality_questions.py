import os
import random

question_id = 1
with open('generated_sql_base_personality_questions.txt', 'w') as f:
    for i in range(70):
        # Every 5th question is for sentiment analysis.
        if random.randint(1, 5) == 1:
            question_type = "textual"
        else:
            question_type = "slider"

        f.write(f'INSERT INTO base_personality_question VALUES({question_id}, "Question {question_id} text", "{question_type}");\n')
        question_id += 1
