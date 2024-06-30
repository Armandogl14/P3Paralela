## Introducción

La presente practica tiene como objetivo proporcionar una plataforma educativa donde los estudiantes puedan diseñar, implementar y evaluar un Framework para simular diversas topologías de redes de procesamiento paralelo. A través de esta práctica, los estudiantes desarrollarán una comprensión profunda de las características y aplicaciones de cada topología, y aprenderán a aplicar técnicas de programación concurrente para mejorar el rendimiento y la seguridad de las operaciones en redes paralelas.

El Framework resultante permitirá a los usuarios configurar y ejecutar simulaciones de redes con diferentes topologías, analizar el comportamiento de los nodos bajo condiciones concurrentes y evaluar el rendimiento en escenarios diversos. Esta herramienta no solo servirá como un recurso educativo valioso, sino también como una base para futuras investigaciones y desarrollos en el ámbito del procesamiento paralelo y la arquitectura de redes.

A lo largo de este documento, se detallarán los componentes clave del Framework, los requisitos de implementación y las instrucciones para su uso, proporcionando una guía completa para la creación de una herramienta robusta y extensible para el estudio de topologías de redes en procesamiento paralelo

## Requisitos de Programación Concurrente
Además de implementar las topologías de redes, se incorporó programación concurrente en las implementaciones utilizando herramientas de concurrencia en Java, tales como:

- **ExecutorService**: Gestionar la ejecución concurrente de los nodos.
- **Thread Safety**: Asegurar operaciones críticas seguras para hilos utilizando mecanismos como `synchronized`, `Locks` o `Concurrent Collections`.
- **Simulación Concurrente**: Los nodos pueden enviar y recibir mensajes concurrentemente.

### Clase `NetworkTopology`
Interfaz base que define los métodos comunes para todas las topologías de redes.

### Subclases para cada Topología de Red
Implementaciones específicas de cada topología de red:
`BusNetwork`

`RingNetwork`

`MeshNetwork`

`StarNetwork`

`HypercubeNetwork`

    public void setupTopology(int numberOfNodes) {
            this.n = (int) (Math.log(numberOfNodes) / Math.log(2));
            this.adjMatrix = new int[numberOfNodes][numberOfNodes];
            for (int i = 0; i < numberOfNodes; i++) {
                for (int j = 0; j < numberOfNodes; j++) {
                    adjMatrix[i][j] = 0;
                }
            }
            for (int i = 0; i < numberOfNodes; i++) {
                for (int j = 0; j < n; j++) {
                    int neighbour = i ^ (1 << j);
                    adjMatrix[i][neighbour] = 1;
                }
            }
        }

`TreeNetwork`
    
     public void setupTopology(int numberOfNodes) {
          this.numberOfNodes = numberOfNodes;
          this.adjList = new ArrayList<>(numberOfNodes);
  
          for (int i = 0; i < numberOfNodes; i++) {
              adjList.add(new ArrayList<>());
          }
  
          // En una red de árbol, cada nodo está conectado a un nodo padre y a cero o más nodos hijos.
          // Aquí, simplemente conectamos cada nodo con su nodo padre (el nodo con índice i está conectado con el nodo con índice i/2).
          for (int i = 1; i < numberOfNodes; i++) {
              int parentNodeId = (i - 1) / 2; // Cambiado a (i - 1) / 2 para obtener correctamente el padre
              adjList.get(parentNodeId).add(i);
              adjList.get(i).add(parentNodeId);
          }
          System.out.println("Configurando topología de red en árbol con " + numberOfNodes + " nodos.");
      }
`FullyConnectedNetwork`
    
    public void setupTopology(int numberOfNodes) {
            this.numberOfNodes = numberOfNodes;
            this.adjList = new ArrayList<>(numberOfNodes);
    
            for (int i = 0; i < numberOfNodes; i++) {
                adjList.add(new ArrayList<>());
            }
    
            // En una red completamente conectada, cada nodo está conectado a todos los demás nodos.
            for (int i = 0; i < numberOfNodes; i++) {
                for (int j = 0; j < numberOfNodes; j++) {
                    if (i != j) {
                        adjList.get(i).add(j);
                    }
                }
            }
            System.out.println("Configurando topología de red completamente conectada con " + numberOfNodes + " nodos.");
        }
`SwitchedNetwork`

    public void setupTopology(int numberOfNodes) {
            this.numberOfNodes = numberOfNodes;
            this.adjList = new ArrayList<>(numberOfNodes);
    
            for (int i = 0; i < numberOfNodes; i++) {
                adjList.add(new ArrayList<>());
            }
    
            // En una red conmutada, cada nodo está conectado a un conmutador.
            // Vamos a suponer que tenemos un conmutador central al cual todos los nodos se conectan.
            // Representamos el conmutador central con el nodo 0.
            for (int i = 1; i < numberOfNodes; i++) {
                adjList.get(0).add(i);  // Conectar el nodo 0 (conmutador) con cada nodo
                adjList.get(i).add(0);  // Conectar cada nodo con el nodo 0 (conmutador)
            }
            System.out.println("Configurando topología de red conmutada con " + numberOfNodes + " nodos.");
    
            // Imprimir la lista de adyacencia para depuración
            printAdjList();
        }
### Clase `Node`
Representa un nodo en la red y contiene atributos como ID, vecinos, etc.

### Clase `Message`
Representa un mensaje que se envía entre nodos en la red.

### Clase `NetworkManager`
Gestiona la creación y ejecución de la red, así como la comunicación entre nodos.

### Clase `Main`
Punto de entrada principal del programa, donde se configuran y ejecutan las topologías de redes.

---

En este proyecto se ejecutaron todas las topologias al mismo tiempo viendo los resultados de los mensajes enviados:
