import os
import random

streams = [
    'agriculture',
    'arts',
    'commerce',
    'fine_arts',
    'health',
    'technical',
    'uniformed',
]

counter = 1
with open('generated_sql_personality_questions.txt', 'w') as f:
    for stream in streams:
        for i in range(10):
            if random.randint(1, 5) == 1:
                question_type = "textual"
            else:
                question_type = "slider"

            f.write(f'INSERT INTO personality_question VALUES({counter}, "{stream}", "Question {counter} text", "{question_type}");\n')
            counter += 1
