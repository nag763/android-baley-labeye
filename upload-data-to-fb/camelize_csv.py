import inflection
import csv

csv_file_name = 'museum-list.csv'
contents_to_keep = ['adr', 'coordonnees_finales', 'cp', 'departement', 'nom_du_musee', 'region', 'telephone1', 'ville']

def camelize_headers(csv_file_name, contents_to_keep):
    unjoined_camelized_header = [] 
    index_list_to_write = []
    with open(csv_file_name, newline='') as f:
            csv_reader = csv.reader(f, delimiter=';')
            csv_headings = next(csv_reader)
            for index, heading in enumerate(csv_headings):
                if heading in contents_to_keep:
                    index_list_to_write.append(index)
                    unjoined_camelized_header.append(inflection.camelize(heading, False))
    
    return (';'.join(unjoined_camelized_header), index_list_to_write)

def format_csv(index_list_to_write):
    """
    Keeps only the wanted values in the given lines
    """
    content = []
    with open(csv_file_name, newline='') as f:
        data = list(csv.reader(f, delimiter=';'))
        for line in data:
            temp_line = []
            for index,value in enumerate(line):            
                if index in index_list_to_write:
                    temp_line.append(value)
            content.append(f"{(';'.join(temp_line))}\n")
    return content

camelized_headers, indexs_to_write = camelize_headers(csv_file_name, contents_to_keep)


content = format_csv(indexs_to_write)


content[0] = f"{camelized_headers}\n"

print(content)


with open(csv_file_name, 'w') as f:
    f.writelines(content)
