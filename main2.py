class Grafo:
    def __init__(self):
        self.vertices = {}

    def adicionar_vertice(self, destino, origens):
        if destino in self.vertices:
            self.vertices[destino] += origens # destino = indice
        else:
            self.vertices[destino] = []
            self.vertices[destino] = origens

    def calcula_custo(self, elemento_atual): # calculando custo de algo
        if elemento_atual == 'hidrogenio': # caso de parada
                return 1
        
        total= 0
        filhos = self.vertices.get(elemento_atual)
        for filho in filhos:
                
                total = total + (filho[0] * self.calcula_custo(filho[1])) # caso recursivo
        return total
    
    def _str_(self):
       return str(self.vertices)

def criar_digrafo(arquivo):
    G = Grafo()

    with open(arquivo) as f:
        for linha in f:
            origens, destino = linha.strip().split(' -> ')
            origens = origens.split()

            quantidade = []
            elemento = []
            for item in origens:
                if item.isnumeric():
                    quantidade.append(int(item))
                else:
                    item.isalpha()
                    elemento.append(item)

            destino = destino.split()[1]
            
            tuplas = list(zip(quantidade, elemento))
            G.adicionar_vertice(destino, tuplas)

    return G

arquivo = r'testes-t10 (1)/testes-t10/casoa5.txt'
digrafo = criar_digrafo(arquivo)
valor_total = digrafo.calcula_custo('ouro') # metodo 
# visualizar_digrafo(digrafo)  
print("Quantidade de hidrogenios:", valor_total)