import os

streams = [
    ('agriculture', 'Agriculture'),
    ('arts', 'Arts & Humanities'),
    ('commerce', 'Commerce'),
    ('fine_arts', 'Fine Arts'),
    ('health', 'Health & Life Sciences'),
    ('technical', 'Technical'),
    ('uniformed', 'Uniformed Services')
]

counter = 1
with open('generated_sql_jobs.txt', 'w') as f:
    for stream in streams:
        for i in range(1, 26):
            stream_id = stream[0]
            stream_title = f'{stream[1]} {i}'
            description = f'Description of {stream_title}'
            f.write(f'INSERT INTO job("id", "title", "description", "stream") VALUES({counter}, "{stream_title}", "{description}", "{stream_id}");\n')
            counter += 1
