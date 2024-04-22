import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URI;
import java.util.Scanner;

public class Conversor {

    private static final String API_KEY = "c99110905aa186b0d56430d3";

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                // Exibe as opções de moedas disponíveis
                System.out.println("\nBem-vindo ao Conversor de Moeda!");
                System.out.println("Escolha a moeda de origem:");
                System.out.println("1. USD - Dólar Americano");
                System.out.println("2. EUR - Euro");
                System.out.println("3. GBP - Libra Esterlina");
                System.out.println("4. JPY - Iene Japonês");
                System.out.println("5. AUD - Dólar Australiano");
                System.out.println("6. BRL - Real Brasileiro");
                System.out.println("0. Sair");
                System.out.print("Digite o número da moeda de origem: ");

                int opcao = scanner.nextInt();
                if (opcao == 0) {
                    System.out.println("Saindo...");
                    break;
                }

                String moedaOrigem = getMoeda(opcao);

                // Obtém a moeda de destino
                System.out.println("Escolha a moeda de destino:");
                System.out.println("1. USD - Dólar Americano");
                System.out.println("2. EUR - Euro");
                System.out.println("3. GBP - Libra Esterlina");
                System.out.println("4. JPY - Iene Japonês");
                System.out.println("5. AUD - Dólar Australiano");
                System.out.println("6. BRL - Real Brasileiro");
                System.out.println("0. Sair");
                System.out.print("Digite o número da moeda de destino: ");
                opcao = scanner.nextInt();
                if (opcao == 0) {
                    System.out.println("Saindo...");
                    break;
                }

                String moedaDestino = getMoeda(opcao);

                // Obtém a quantidade a ser convertida
                System.out.print("Digite a quantidade a ser convertida: ");
                double quantidade = scanner.nextDouble();

                // Obtém a taxa de câmbio usando a API
                double taxaDeCambio = getTaxaDeCambio(moedaOrigem, moedaDestino);

                // Calcula o valor convertido
                double valorConvertido = quantidade * taxaDeCambio;

                // Exibe o resultado
                System.out.println(quantidade + " " + moedaOrigem + " equivalem a " + valorConvertido + " " + moedaDestino);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String getMoeda(int opcao) {
        switch (opcao) {
            case 1:
                return "USD";
            case 2:
                return "EUR";
            case 3:
                return "GBP";
            case 4:
                return "JPY";
            case 5:
                return "AUD";
            case 6:
                return "BRL";
            default:
                throw new IllegalArgumentException("Opção inválida.");
        }
    }

    private static double getTaxaDeCambio(String moedaOrigem, String moedaDestino) throws IOException {
        //String urlStr = "https://v6.exchangerate-api.com/v6/c99110905aa186b0d56430d3/latest/" + moedaOrigem;
        //String urlStr = "https://v6.exchangerate-api.com/v6/"+ API_KEY +"/latest/" + moedaOrigem; //+ "&symbols=" + moedaDestino;
        URI uri = URI.create("https://v6.exchangerate-api.com/v6/" + API_KEY + "/latest/" + moedaOrigem);
        URL url = uri.toURL();
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setConnectTimeout(5000);
        conn.setReadTimeout(5000);

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
            String line;
            StringBuilder response = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }

            // Extrair a taxa de câmbio do texto da resposta
            String jsonResponse = response.toString();
            int startIndex = jsonResponse.indexOf(moedaDestino);
            int endIndex = jsonResponse.indexOf(",", startIndex);
            String taxaStr = jsonResponse.substring(startIndex + 5, endIndex);
            return Double.parseDouble(taxaStr);

        }
    }
}
