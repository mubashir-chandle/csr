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
    for correct_option, category in enumerate(categories, 1):
        for i in range(10):
            x = random.randint(1, 5)
            if x == 1:
                f.write(f'INSERT INTO aptitude_question VALUES({counter}, "{category}", "Question {counter} text", "Option 1", "Option 2", "Option 3", "Option 4", "Option 5", {correct_option}, "no_image");\n')
            elif x == 2:
                f.write(f'INSERT INTO aptitude_question("id",  "category", "text", "option_1", "option_2", "option_3", "option_4", "option_5", "correct_option") VALUES({counter}, "{category}", "Question {counter} text", "Option 1", "Option 2", "Option 3", "Option 4", "Option 5", {correct_option});\n')
            else:
                f.write(f'INSERT INTO aptitude_question("id",  "category", "text", "option_1", "option_2", "option_3", "option_4", "correct_option") VALUES({counter}, "{category}", "Question {counter} text", "Option 1", "Option 2", "Option 3", "Option 4", {correct_option});\n')
            counter += 1
