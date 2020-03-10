import os
import random

categories = [
    'category 1',
    'category 2',
    'category 3',
    'category 4',
]

counter = 1
with open('generated_sql_aptitude_questions.txt', 'w') as f:
    for category in categories:
        for i in range(10):
            correct_option = random.randint(1, 4)
            if random.randint(1, 5) == 1:
                f.write(f'INSERT INTO aptitude_question VALUES({counter}, "{category}", "Question {counter} text", "Option 1", "Option 2", "Option 3", "Option 4", {correct_option}, "no_image");\n')
            else:
                f.write(f'INSERT INTO aptitude_question("id",  "category", "text", "option_1", "option_2", "option_3", "option_4", "correct_option") VALUES({counter}, "{category}", "Question {counter} text", "Option 1", "Option 2", "Option 3", "Option 4", {correct_option});\n')
            counter += 1
