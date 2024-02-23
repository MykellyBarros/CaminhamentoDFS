import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class CalculoHidrogenioParaOuro {

    static class Aresta {
        String de;
        String para;
        int quantidade;

        Aresta(String de, String para, int quantidade) {
            this.de = de;
            this.para = para;
            this.quantidade = quantidade;
        }
    }

    static int calcularHidrogenioParaOuro(List<Aresta> arestas, Map<String, Integer> quantidadeElementos) {
        Map<String, Integer> quantidadeNecessaria = new HashMap<>();
        quantidadeNecessaria.put("ouro", 1);

        while (!quantidadeNecessaria.isEmpty()) {
            Map<String, Integer> novoNecessario = new HashMap<>();

            for (Map.Entry<String, Integer> entry : quantidadeNecessaria.entrySet()) {
                String elementoAtual = entry.getKey();
                int quantidadeAtual = entry.getValue();

                for (Aresta aresta : arestas) {
                    if (aresta.para.equals(elementoAtual)) {
                        int novaQuantidade = quantidadeAtual * aresta.quantidade;

                        quantidadeElementos.put(aresta.de, quantidadeElementos.getOrDefault(aresta.de, 0) + novaQuantidade);

                        if (!aresta.de.equals("hidrogenio")) {
                            novoNecessario.put(aresta.de, novoNecessario.getOrDefault(aresta.de, 0) + novaQuantidade);
                        }
                    }
                }
            }

            quantidadeNecessaria = novoNecessario;
        }

        return quantidadeElementos.getOrDefault("hidrogenio", 0);
    }

    public static void main(String[] args) {
        List<Aresta> arestas = new ArrayList<>();
        Map<String, Integer> quantidadeElementos = new HashMap<>();
        List<String> multiStepConversions = new ArrayList<>();

        try {
            File arquivo = new File("casoteste.txt");
            Scanner scanner = new Scanner(arquivo);

            boolean reachedSecondPart = false;

            while (scanner.hasNextLine()) {
                String linha = scanner.nextLine();
                String[] partes = linha.split(" ");

                if (!reachedSecondPart) {
                    int quantidade = Integer.parseInt(partes[0]);
                    String para = partes[partes.length - 1];

                    for (int i = 1; i < partes.length - 2; i++) {
                        if (partes[i].equals("->")) {
                            continue;
                        }

                        if (Character.isDigit(partes[i].charAt(0))) {
                            break;
                        }

                        String elemento = partes[i];
                        arestas.add(new Aresta(elemento, para, quantidade));
                        quantidadeElementos.putIfAbsent(elemento, 0);
                    }

                    if (partes[partes.length - 1].equals("ouro")) {
                        reachedSecondPart = true;
                    }
                } else {
                    multiStepConversions.add(linha);
                }
            }

            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("Arquivo não encontrado.");
            e.printStackTrace();
        }

        // Processar a segunda parte das conversões
        for (String linha : multiStepConversions) {
            String[] partes = linha.split(" ");
            int quantidadeFinal = Integer.parseInt(partes[partes.length - 1]);

            List<String> elementos = new ArrayList<>();
            List<Integer> quantidades = new ArrayList<>();

            for (int i = 0; i < partes.length - 3; i++) {
                if (Character.isDigit(partes[i].charAt(0))) {
                    quantidades.add(Integer.parseInt(partes[i]));
                } else {
                    elementos.add(partes[i]);
                }
            }

            int quantidadeElementosFinal = 1;

            for (int q : quantidades) {
                quantidadeElementosFinal *= q;
            }

            for (String elem : elementos) {
                quantidadeElementosFinal += quantidadeElementos.getOrDefault(elem, 0);
            }

            quantidadeElementos.put(partes[partes.length - 3], quantidadeElementosFinal);
        }

        int quantidadeHidrogenioParaOuro = calcularHidrogenioParaOuro(arestas, quantidadeElementos);

        System.out.println("\nQuantidade de hidrogênio para produzir uma unidade de ouro: " + quantidadeHidrogenioParaOuro);
    }
}
