import json
import os

def read_jsons():
    return [file for file in os.listdir() if file.endswith('.json')]
    

def put_id_in_body(path):


    data = json.load(open(os.getcwd() +"/"+ path))

    list_data = list(data)

    output = []

    i = 0
    while i < len(list_data):
        item = list(list_data[i])
        id = item[0]
        new_body = list(list_data[i].values())[0]
        new_body['id'] = id

        output.append(new_body)
        i += 1
    return output



def main():

    jsons = read_jsons()    
    
    for filename in jsons:
        
        output = put_id_in_body(filename)
        
        with open("transformed/redone" + filename, 'w', encoding='utf8') as outfile:
            json.dump(output, outfile)


    
if __name__ == "__main__":
    main()