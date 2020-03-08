import os

categories = [
    'category 1',
    'category 2',
    'category 3',
    'category 4',
]

counter = 1
with open('generated_aptitude_questions.txt', 'w') as f:
    for category in categories:
        for i in range(10):
            if counter % 7 == 0:
                f.write(f'INSERT INTO aptitude_question VALUES({counter}, "Question {counter} text", "Option 1", "Option 2", "Option 3", "Option 4", "placeholder", "{category}");\n')
            else:
                f.write(f'INSERT INTO aptitude_question("id", "question", "option_1", "option_2", "option_3", "option_4", "category") VALUES({counter}, "Question {counter} text", "Option 1", "Option 2", "Option 3", "Option 4", "{category}");\n')
            counter += 1
