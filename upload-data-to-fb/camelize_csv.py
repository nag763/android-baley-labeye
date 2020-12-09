import inflection
import csv

csv_file_name = 'museum-list.csv'
unjoined_camelized_header = [] 
content = ''

with open(csv_file_name, newline='') as f:
        csv_reader = csv.reader(f)
        csv_headings = next(csv_reader)
        for heading in csv_headings:
            unjoined_camelized_header.append(inflection.camelize(heading, False))


with open(csv_file_name, newline='') as f:
    content = f.readlines()

content[0] = ';'.join(unjoined_camelized_header)
content[0] = f'{content[0]}\n'
print(unjoined_camelized_header)


with open(csv_file_name, 'w') as f:
    f.writelines(content)
