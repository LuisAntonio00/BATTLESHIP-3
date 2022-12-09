package battleship;
import java.util.ArrayList;
import java.util.Scanner;
class Ship {
    ArrayList<String> positions;

    Ship(String c1, String c2, int shipLength) {
        positions = new ArrayList<>();
        StringBuilder c1Builder = new StringBuilder(c1);
        if (c1.charAt(0) == c2.charAt(0)) {
            for (int i = 0; i < shipLength; i++) {
                positions.add(c1Builder.toString());
                c1Builder.replace(1, c1.length(), Integer.toString(Integer.parseInt(c1Builder.substring(1)) + 1));
            }
        } else { //eg A1, E1, 5
            for (int i = 0; i < shipLength; i++) {
                positions.add(c1Builder.toString());
                c1Builder.replace(0, 1, Character.toString(c1Builder.charAt(0) + 1));
            }
        }
    }
}
class Map {
    String[][] map;
    ArrayList<Ship> ships;

    Map() {
        this.map = new String[11][11];
        this.ships = new ArrayList<>();
    }

    public void fill() {
        map[0][0] = " ";

        //from 1 to 10
        for (int i = 1; i < 11; i++) {
            map[0][i] = Integer.toString(i);
        }

        map[1][0] = "A";
        map[2][0] = "B";
        map[3][0] = "C";
        map[4][0] = "D";
        map[5][0] = "E";
        map[6][0] = "F";
        map[7][0] = "G";
        map[8][0] = "H";
        map[9][0] = "I";
        map[10][0] = "J";

        for (int i = 1; i < 11; i++) {
            for (int j = 1; j < 11; j++) {
                map[i][j] = "~";
            }
        }
    }

    public void display() {
        for (int i = 0; i < 11; i++) {
            for (int j = 0; j < 11; j++) {
                System.out.print(" " + map[i][j]);
            }
            System.out.println();
        }
    }

    public void displayWithFog() {
        for (int i = 0; i < 11; i++) {
            for (int j = 0; j < 11; j++) {
                if (map[i][j].equals("O")) {
                    System.out.print(" ~");
                } else {
                    System.out.print(" " + map[i][j]);
                }
            }
            System.out.println();
        }
    }

    public void placeShips() {
        Scanner scanner = new Scanner(System.in);
        int shipLength = 5;
        int counter = 0;
        boolean shipsArePlaced = false;
        System.out.println("Introduce las cordenadas de PROMETHEUS (Mide 5 espacios): ");

        while (!shipsArePlaced) {
            String c1 = scanner.next();
            String c2 = scanner.next();
            if (this.placeShip(c1, c2, shipLength) == 1) {
                this.display();
                counter++;
                switch (counter) {
                    case 1:
                        System.out.println("Introduce las cordenadas de LA NIÑA (Mide 4 espacios): ");
                        shipLength = 4;
                        break;
                    case 2:
                        System.out.println("Introduce las cordenadas de LA PINTA (Mide 3 espacios): ");
                        shipLength = 3;
                        break;
                    case 3:
                        System.out.println("Introduce las cordenadas de LA SANTAMARIA (Mide 3 espacios): ");
                        break;
                    case 4:
                        System.out.println("Introduce las cordenadas del PERLA NEGRA (Mide 2 espacios) ");
                        shipLength = 2;
                        break;
                    default:
                        shipsArePlaced = true;
                        System.out.println("Presiona ENTER para pasar tu turno");
                        break;
                }
            }
        }
    }

    private int placeShip(String c1, String c2, int shipLength) {
        char c1X;
        int c1Y;

        char c2X;
        int c2Y;

        try {
            c1X = c1.toUpperCase().charAt(0);
            c1Y = Integer.parseInt(c1.toUpperCase().substring(1));

            c2X = c2.toUpperCase().charAt(0);
            c2Y = Integer.parseInt(c2.toUpperCase().substring(1));
        } catch (Exception e) {
            System.out.println("Error! Introduce las cordenadas de nuevo");
            System.out.println("Ejemplo (A1): ");
            return -1;
        }


        //if coordinates are not in one line -> throw error
        if (c1X != c2X && c1Y != c2Y || c1X == c2X && c1Y == c2Y) {
            System.out.println("¡Error! ¡Los barcos deben estar en la misma línea! Intentalo de nuevo: ");
            return -1;
        }

        //if coordinates are out of map
        if (c1X < 'A' || c1X > 'J' || c2X < 'A' || c2X > 'J' || c1Y < 0 || c1Y > 10 || c2Y < 0 || c2Y > 10) {
            System.out.println("'¡Error! ¡Las coordenadas deben ser A-J y 1-10! Intentalo de nuevo: ");
            return -1;
        }

        //if coordinates are reverse -> reverse them
        if (c1X > c2X || c1Y > c2Y) {
            char bufX = c1X;
            int bufY = c1Y;
            String buf = c1;
            c1 = c2;
            c2 = buf;
            c1X = c2X;
            c1Y = c2Y;
            c2X = bufX;
            c2Y = bufY;
        }


        if (c1X == c2X) {
            if ((c2Y - c1Y + 1) != shipLength) {
                System.out.println("¡Error! El barco debe tener exactamente un tamaño de: " + shipLength + " y ahora su tamaño es de: " + (c2X - c1X + 1) + "intentalo de nuevo: ");
                return -1;
            }
            int cor1XAsNumber = toNumber(c1X);
            for (int i = cor1XAsNumber - 1; i <= cor1XAsNumber + 1; i++) {
                for (int j = c1Y - 1; j <= c1Y + shipLength; j++) {
                    if (i < 1 || i > 10 || j < 1 || j > 10) {
                        continue;
                    }
                    if ("O".equals(map[i][j]) || "X".equals(map[i][j]) || "M".equals(map[i][j])) {
                        System.out.println("Lo colocaste demasiado cerca de otro barco. Mejor ponlo en otro lugar :");
                        return -1;
                    }
                }
            }
            for (int i = c1Y; i < c1Y + shipLength; i++) {
                map[toNumber(c1X)][i] = "O";
            }
        } else {
            if ((c2X - c1X + 1) != shipLength) {
                System.out.println("¡Error! El barco debe tener exactamente un tamaño de: " + shipLength + " y ahora su tamaño es de: " + (c2X - c1X + 1) + "intentalo de nuevo: ");
                return -1;
            }
            int cor1XAsNumber = toNumber(c1X);
            for (int i = cor1XAsNumber - 1; i <= cor1XAsNumber + shipLength; i++) {
                for (int j = c1Y - 1; j <= c1Y + 1; j++) {
                    if (i < 1 || i > 10 || j < 1 || j > 10) {
                        continue;
                    }
                    if ("O".equals(map[i][j]) || "X".equals(map[i][j]) || "M".equals(map[i][j])) {
                        System.out.println("Lo colocaste demasiado cerca de otro barco. Mejor ponlo en otro lugar :");
                        return -1;
                    }
                }
            }
            for (int i = toNumber(c1X); i < toNumber(c1X) + shipLength; i++) {
                map[i][c1Y] = "O";
            }
        }
        ships.add(new Ship(c1, c2, shipLength));
        return 1;

    }

    private int toNumber(char c) {
        switch (c) {
            case 'A':
                return 1;
            case 'B':
                return 2;
            case 'C':
                return 3;
            case 'D':
                return 4;
            case 'E':
                return 5;
            case 'F':
                return 6;
            case 'G':
                return 7;
            case 'H':
                return 8;
            case 'I':
                return 9;
            case 'J':
                return 10;
            default:
                return -1;
        }
    }

    public void shoot(String c1) {
        int c1X = toNumber(c1.charAt(0));
        int c1Y = Integer.parseInt(c1.substring(1));

        if (c1X < 1 || c1X > 10 || c1Y < 1 || c1Y > 10) {
            System.out.println("¡Error! ¡Has introducido las coordenadas incorrectas! Intentar otra vez:");
            return;
        }

        switch (map[c1X][c1Y]) {
            case "O":
                map[c1X][c1Y] = "X";
                Ship ship = deleteCell(c1);
                ships.remove(ship);
                if (ships.isEmpty()) {
                    System.out.println("Hundiste el último barco! acabaste con tu enemigo ¡¡¡HURRA!!!");
                } else if (ship != null) {
                    System.out.println("¡Hundiste el barco! elige otro punto y ATACA :)");
                } else {
                    System.out.println("LE DISTE !!! :)");
                }
                break;
            case "~":
                map[c1X][c1Y] = "M";
                System.out.println("FALLASTE :(");
                break;
            case "X":
                System.out.println("¡Error! ya disparaste aqui");
                break;
        }

    }

    public void promptEnterKey() {
        System.out.println("Presione \"ENTER\" para continuar...");
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
    }

    Ship deleteCell(String cell) {
        for (Ship ship : ships) {
            if (ship.positions.contains(cell)) {
                ship.positions.remove(cell);
                if (ship.positions.isEmpty()) {
                    return ship;
                }
            }
        }
        return null;
    }
}
public class Main {
    public static void main(String[] args) {
        System.out.println("Bienvenidos a este juego");
        Scanner scanner = new Scanner(System.in);
        Scanner name = new Scanner(System.in);
        Map map1 = new Map();
        map1.fill();
        System.out.println();
        System.out.println("Introduzca su nombre jugador 1");
        String nombre = name.nextLine();
        System.out.println(          nombre);
        System.out.println("Introduzca sus barcos en el tablero");
        map1.display();
        map1.placeShips();
        map1.promptEnterKey();

        Map map2 = new Map();
        map2.fill();
        System.out.println("Introduzca su nombre jugador 1");
        String nombre2 = name.nextLine();
        System.out.println(          nombre2);
        System.out.println("Introduzca sus barcos en el tablero");
        map2.display();
        map2.placeShips();
        map2.promptEnterKey();

        boolean player1Turn = true;
        while (!map1.ships.isEmpty() || !map2.ships.isEmpty()) {
            if (player1Turn) {
                map2.displayWithFog();
                System.out.println("---------------------");
                map1.display();
                System.out.println("Es el turno de: "+nombre);
                String c1 = scanner.next();
                map2.shoot(c1);
                map2.promptEnterKey();
                player1Turn = false;
            } else {
                map1.displayWithFog();
                System.out.println("---------------------");
                map2.display();
                System.out.println("Es el turno de: "+nombre2);
                String c1 = scanner.next();
                map1.shoot(c1);
                map1.promptEnterKey();
                player1Turn = true;
            }
        }

        if (map2.ships.isEmpty()) {
            System.out.println(nombre+"  WIN");
            System.out.println("FATALITY");
        } else {
            System.out.println(nombre2+"  WIN");
            System.out.println("FATALITY");
        }
    }
}