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

question_id = 1
base_question_id = 1

with open('generated_sql_stream_questions.txt', 'w') as f:
    for stream in streams:
        for i in range(1):
            if question_id % 3 == 1:
                importance = 'low'
            elif question_id % 3 == 2:
                importance = 'medium'
            else:
                importance = 'high'

            f.write(f'INSERT INTO stream_question VALUES({question_id}, {base_question_id}, "{stream}", "{importance}");\n')
            question_id += 1
            base_question_id += 1
